package com.alexeygrigorev.dstools.wrappers.smile;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.RegressionModel;

import smile.regression.Regression;

public class SmileRegressionWrapper implements RegressionModel {

    private final Regression<double[]> regression;

    public SmileRegressionWrapper(Regression<double[]> regression) {
        this.regression = regression;
    }

    @Override
    public void fit(Dataset dataset) {
        throw new UnsupportedOperationException("Smile models are already fit");
    }

    @Override
    public double[] predict(Dataset dataset) {
        return predict(regression, dataset);
    }

    public static double[] predict(Regression<double[]> model, Dataset dataset) {
        double[][] X = dataset.getX();
        double[] result = new double[X.length];

        for (int i = 0; i < X.length; i++) {
            result[i] = model.predict(X[i]);
        }

        return result;
    }

}
