package com.alexeygrigorev.dstools.wrappers.smile;

import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.Validate;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.RegressionModel;
import com.alexeygrigorev.dstools.opt.ParametrizedModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import smile.regression.RidgeRegression;

public class SmileRidgeRegression implements ParametrizedModel, RegressionModel {

    public static final String LAMBDA = "lambda";

    private static final Map<String, Class<?>> PARAM_TYPE_CHECKER = ImmutableMap.of(LAMBDA, Double.class);
    private static final Map<String, Object> DEFAULTS = ImmutableMap.of(LAMBDA, 1.0);

    private final Map<String, Object> params = Maps.newHashMap();

    private RidgeRegression model;

    public SmileRidgeRegression() {
    }

    public SmileRidgeRegression(double lambda) {
        params.put(LAMBDA, lambda);
    }

    @Override
    public <E> void setParam(String name, E value) {
        Validate.isTrue(PARAM_TYPE_CHECKER.containsKey(name), "unknown property " + name);
        Class<?> expectedClass = PARAM_TYPE_CHECKER.get(name);
        Validate.isTrue(value.getClass().equals(expectedClass), "the class of " + name + " shoud be " + expectedClass);

        params.put(name, value);
    }

    @Override
    public void fit(Dataset dataset) {
        double lambda = (double) getParam(LAMBDA);
        this.model = new RidgeRegression(dataset.getX(), dataset.getY(), lambda);
    }

    @Override
    public double[] predict(Dataset dataset) {
        Validate.validState(model != null, "model is not fit yet. Call fit() before using predict()");

        double[][] X = dataset.getX();
        return IntStream.range(0, dataset.length()).parallel().mapToDouble(i -> model.predict(X[i])).toArray();
    }

    private Object getParam(String name) {
        return params.getOrDefault(name, DEFAULTS.get(name));
    }

}
