package com.alexeygrigorev.dstools.opt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexeygrigorev.dstools.cv.Fold;
import com.alexeygrigorev.dstools.metrics.Metric;
import com.alexeygrigorev.dstools.models.Model;
import com.alexeygrigorev.dstools.opt.ParameterOptimizer.BestParams;
import com.alexeygrigorev.dstools.opt.ParameterOptimizer.OptimizationCallback;
import com.alexeygrigorev.dstools.regression.RegressionModel;

public class RandomSearch {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomSearch.class);

    private int trials = 10;
    private boolean minimize = true;
    private boolean verbose = false;

    private SearchSpace searchSpace = new SearchSpace();

    private Random random = new Random();

    private Model model;
    private Metric metric;
    private List<Fold> folds;

    private OptimizationCallback<Model> callback;

    public static RandomSearch on(Model model) {
        RandomSearch randomSearch = new RandomSearch();
        return randomSearch.model(model);
    }

    public RandomSearch numTrials(int trials) {
        this.trials = trials;
        return this;
    }

    public RandomSearch verbose() {
        this.verbose = true;
        return this;
    }

    public RandomSearch seed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public <E> RandomSearch parameter(String paramName, Domain<E> domain) {
        searchSpace.addParam(paramName, domain);
        return this;
    }

    public RandomSearch model(Model model) {
        this.model = model;
        return this;
    }

    public RandomSearch metric(Metric metric) {
        this.metric = metric;
        return this;
    }

    public RandomSearch optimizeOnHoldOut(Fold validation) {
        return optimizeOnCV(Collections.singletonList(validation));
    }

    public RandomSearch optimizeOnCV(List<Fold> folds) {
        this.folds = folds;
        return this;
    }

    public BestParams minimize() {
        this.minimize = true;
        prepareCallback();
        return search();
    }

    private void prepareCallback() {
        if (model instanceof RegressionModel) {
            this.callback = regressionCallback();
        } else {
            throw new IllegalStateException("unknown model type or model is not set");
        }
    }

    private OptimizationCallback<Model> regressionCallback() {
        return (m) -> {
            RegressionModel regression = (RegressionModel) m;

            return folds.stream().mapToDouble(fold -> {
                regression.fit(fold.getTrain());
                double[] predict = regression.predict(fold.getTest());
                return metric.calculate(fold.getTest().getY(), predict);
            }).average().getAsDouble();
        };
    }

    public BestParams maximize() {
        this.minimize = false;
        return search();
    }

    private BestParams search() {
        Map<String, Domain<?>> searchSpaceParams = searchSpace.allParams();

        // TODO: make it parallel
        Stream<Pair<Params, Double>> results = IntStream.range(0, trials).mapToObj(i -> {
            Params params = new Params();

            for (Map.Entry<String, Domain<?>> e : searchSpaceParams.entrySet()) {
                String paramName = e.getKey();
                Domain<?> paramDomain = e.getValue();
                params.setParam(paramName, paramDomain.sample(random));
            }

            model.setParams(params);
            double result = callback.execute(model);

            if (verbose) {
                LOGGER.info("{} obtained with {}", result, params);
            }

            return Pair.of(params, result);
        });

        Pair<Params, Double> best = results.min(comparator()).get();

        if (verbose) {
            LOGGER.info("best result is {} obtained with params {}", best.getRight(), best.getLeft());
        }

        return new BestParams(best.getKey(), best.getValue());
    }

    private Comparator<? super Pair<Params, Double>> comparator() {
        if (minimize) {
            return (a, b) -> Double.compare(a.getRight(), b.getRight());
        } else {
            return (a, b) -> -Double.compare(a.getRight(), b.getRight());
        }
    }
}
