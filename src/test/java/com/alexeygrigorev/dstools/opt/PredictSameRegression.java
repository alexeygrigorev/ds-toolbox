package com.alexeygrigorev.dstools.opt;

import java.util.Arrays;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.regression.RegressionModel;

class PredictSameRegression implements RegressionModel {
    double hyperparam = 0;

    @Override
    public <E> void setParam(String name, E value) {
        hyperparam = (double) value;
    }

    @Override
    public void fit(Dataset dataset) {
        // well done!
    }

    @Override
    public double[] predict(Dataset dataset) {
        double[] res = new double[dataset.length()];
        Arrays.fill(res, hyperparam);
        return res;
    }
}