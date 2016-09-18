package com.alexeygrigorev.dstools.opt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.RegressionModel;

class SumRegression implements RegressionModel, ParametrizedModel {
    private final Map<String, Double> params = new HashMap<>();

    @Override
    public <E> void setParam(String name, E value) {
        params.put(name, (Double) value);
    }

    @Override
    public void fit(Dataset dataset) {
        // well done!
    }

    @Override
    public double[] predict(Dataset dataset) {
        double[] res = new double[dataset.length()];
        double sum = params.values().stream().mapToDouble(d -> d).sum();
        Arrays.fill(res, sum);
        return res;
    }
}