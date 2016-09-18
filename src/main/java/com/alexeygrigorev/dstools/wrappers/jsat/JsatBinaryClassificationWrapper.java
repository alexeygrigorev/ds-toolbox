package com.alexeygrigorev.dstools.wrappers.jsat;

import java.util.ArrayList;
import java.util.List;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.BinaryClassificationModel;

import jsat.classifiers.CategoricalData;
import jsat.classifiers.CategoricalResults;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.Classifier;
import jsat.classifiers.DataPoint;
import jsat.classifiers.DataPointPair;
import jsat.linear.DenseVector;

public class JsatBinaryClassificationWrapper implements BinaryClassificationModel {

    private final Classifier classifier;

    public JsatBinaryClassificationWrapper(Classifier classifier) {
        this.classifier = classifier;
    }

    @Override
    public void fit(Dataset dataset) {
        classifier.trainC(wrapDataset(dataset));
    }

    @Override
    public double[] predict(Dataset dataset) {
        return predict(classifier, dataset);
    }

    public static ClassificationDataSet wrapDataset(Dataset dataset) {
        double[][] X = dataset.getX();
        double[] y = dataset.getY();
        CategoricalData binary = new CategoricalData(2);

        List<DataPointPair<Integer>> data = new ArrayList<>(X.length);
        for (int i = 0; i < X.length; i++) {
            int target = (int) y[i];
            DataPoint row = new DataPoint(new DenseVector(X[i]));
            data.add(new DataPointPair<Integer>(row, target));
        }

        return new ClassificationDataSet(data, binary);
    }

    public static double[] predict(Classifier model, Dataset dataset) {
        double[][] X = dataset.getX();
        double[] result = new double[X.length];

        for (int i = 0; i < X.length; i++) {
            DenseVector vector = new DenseVector(X[i]);
            DataPoint point = new DataPoint(vector);
            CategoricalResults out = model.classify(point);
            result[i] = out.getProb(1);
        }

        return result;
    }

}
