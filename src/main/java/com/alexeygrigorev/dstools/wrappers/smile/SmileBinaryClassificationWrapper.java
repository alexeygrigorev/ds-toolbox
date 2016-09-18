package com.alexeygrigorev.dstools.wrappers.smile;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.BinaryClassificationModel;

import smile.classification.SoftClassifier;

public class SmileBinaryClassificationWrapper implements BinaryClassificationModel {

    private final SoftClassifier<double[]> model;

    public SmileBinaryClassificationWrapper(SoftClassifier<double[]> model) {
        this.model = model;
    }

    @Override
    public void fit(Dataset dataset) {
        throw new UnsupportedOperationException("Smile models are already fit");
    }

    @Override
    public double[] predict(Dataset dataset) {
        return predict(model, dataset);
    }

    public static double[] predict(SoftClassifier<double[]> model, Dataset dataset) {
        double[][] X = dataset.getX();
        double[] result = new double[X.length];

        double[] probs = new double[2];
        for (int i = 0; i < X.length; i++) {
            model.predict(X[i], probs);
            result[i] = probs[1];
        }

        return result;
    }

}
