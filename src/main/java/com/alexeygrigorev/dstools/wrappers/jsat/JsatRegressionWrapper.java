package com.alexeygrigorev.dstools.wrappers.jsat;

import java.util.ArrayList;
import java.util.List;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.RegressionModel;

import jsat.classifiers.DataPoint;
import jsat.classifiers.DataPointPair;
import jsat.linear.DenseVector;
import jsat.regression.RegressionDataSet;
import jsat.regression.Regressor;

public class JsatRegressionWrapper implements RegressionModel {

    private final Regressor regressor;

    public JsatRegressionWrapper(Regressor regressor) {
        this.regressor = regressor;
    }

    @Override
    public void fit(Dataset dataset) {
        RegressionDataSet jsatDataset = wrapDataset(dataset);
        regressor.train(jsatDataset);
    }

    @Override
    public double[] predict(Dataset dataset) {
        return predict(regressor, dataset);
    }

    public static double[] predict(Regressor model, Dataset dataset) {
        double[][] X = dataset.getX();
        double[] result = new double[X.length];

        for (int i = 0; i < X.length; i++) {
            DenseVector vector = new DenseVector(X[i]);
            DataPoint point = new DataPoint(vector);
            result[i] = model.regress(point);
        }

        return result;
    }

    public static RegressionDataSet wrapDataset(Dataset dataset) {
        double[][] X = dataset.getX();
        double[] y = dataset.getY();

        List<DataPointPair<Double>> data = new ArrayList<>(X.length);

        for (int i = 0; i < X.length; i++) {
            DataPoint row = new DataPoint(new DenseVector(X[i]));
            data.add(new DataPointPair<Double>(row, y[i]));
        }

        return new RegressionDataSet(data);
    }

}
