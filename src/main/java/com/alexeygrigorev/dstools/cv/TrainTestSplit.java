package com.alexeygrigorev.dstools.cv;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.Validate;

public class TrainTestSplit {

    public static TrainTestSplit split(double[][] X, double[] y, double testRatio) {
        return split(X, y, testRatio, System.currentTimeMillis());
    }

    public static TrainTestSplit split(double[][] X, double[] y, double testRatio, long seed) {
        Validate.notNull(X, "X must not be null");
        Validate.notNull(y, "y must not be null");
        Validate.isTrue(X.length == y.length, "X and y must have the same size");
        Validate.isTrue(testRatio > 0.0 && testRatio < 1.0, "testRatio must be in (0, 1) interval");

        int expectedTrainSize = (int) (X.length * (1 - testRatio));
        int expectedTestSize = (int) (X.length * testRatio);

        List<double[]> trainX = new ArrayList<>(expectedTrainSize);
        List<double[]> testX = new ArrayList<>(expectedTestSize);

        List<Double> trainY = new ArrayList<>(expectedTrainSize);
        List<Double> testY = new ArrayList<>(expectedTestSize);

        Random rnd = new Random(seed);
        for (int i = 0; i < X.length; i++) {
            if (rnd.nextFloat() <= testRatio) {
                testX.add(X[i]);
                testY.add(y[i]);
            } else {
                trainX.add(X[i]);
                trainY.add(y[i]);
            }
        }

        int trainSize = trainX.size();
        double[][] trainXres = new double[trainSize][];
        double[] trainYres = new double[trainSize];
        for (int i = 0; i < trainSize; i++) {
            trainXres[i] = trainX.get(i);
            trainYres[i] = trainY.get(i);
        }

        int testSize = testX.size();
        double[][] testXres = new double[testSize][];
        double[] testYres = new double[testSize];
        for (int i = 0; i < testSize; i++) {
            testXres[i] = testX.get(i);
            testYres[i] = testY.get(i);
        }

        return new TrainTestSplit(X, y, trainXres, trainYres, testXres, testYres);
    }

    private final double[][] fullX;
    private final double[] fullY;
    private final double[][] trainX;
    private final double[] trainY;
    private final double[][] testX;
    private final double[] testY;

    public TrainTestSplit(double[][] fullX, double[] fullY, double[][] trainX, double[] trainY, double[][] testX,
            double[] testY) {
        this.fullX = fullX;
        this.fullY = fullY;
        this.trainX = trainX;
        this.trainY = trainY;
        this.testX = testX;
        this.testY = testY;
    }

    public double[][] getFullX() {
        return fullX;
    }

    public double[] getFullY() {
        return fullY;
    }

    public double[][] getTrainX() {
        return trainX;
    }

    public double[][] getTestX() {
        return testX;
    }

    public double[] getTestY() {
        return testY;
    }

    public double[] getTrainY() {
        return trainY;
    }
}
