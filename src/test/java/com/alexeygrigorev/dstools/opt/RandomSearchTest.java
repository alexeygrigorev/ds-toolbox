package com.alexeygrigorev.dstools.opt;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.data.Datasets;
import com.alexeygrigorev.dstools.metrics.RegressionMetrics;
import com.alexeygrigorev.dstools.opt.ParameterOptimizer.BestParams;

public class RandomSearchTest {

    @Test
    public void oneDoubleParameterRegression() {
        double[][] X = { { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } };
        double[] y = { 1, 2, 1, 1, 2, 1, 1, 2, 1 };

        PredictSameRegression regression = new PredictSameRegression();
        Dataset data = Datasets.of(X, y);

        BestParams bestParams = RandomSearch.on(regression)
                    .parameter("hyperparam", Domains.uniform(-1.0, 1.0))
                    .numTrials(100)
                    .seed(10101)
                    .verbose()
                    .metric(RegressionMetrics.RMSE)
                    .optimizeOnCV(data.kfold(2))
                    .minimize();

        double bestScore = bestParams.getBestValue();
        assertTrue(bestScore <= 0.65);

        double bestParamValue = bestParams.getParams().getDouble("hyperparam");
        assertTrue(bestParamValue >= 0.9); // should be close to 1
    }

    @Test
    public void multipleParameterRegression() {
        double[][] X = { { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } };
        double[] y = { 1, 2, 1, 1, 2, 1, 1, 2, 1 };

        PredictSameRegression regression = new PredictSameRegression();
        Dataset data = Datasets.of(X, y);

        BestParams bestParams = RandomSearch.on(regression)
                .parameter("param1", Domains.linSpace(-1, 1, 21))
                .parameter("param2", Domains.uniform(1, 2))
                .parameter("param3", Domains.linSpace(0, 1, 11))
                .verbose()
                .numTrials(100)
                .seed(10101)
                .metric(RegressionMetrics.RMSE)
                .optimizeOnCV(data.kfold(2))
                .minimize();

        double bestScore = bestParams.getBestValue();
        assertTrue(bestScore <= 0.5);
    }

}
