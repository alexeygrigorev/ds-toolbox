package com.alexeygrigorev.dstools.cv;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Fold {

    private final double[][] trainX;
    private final double[] trainY;
    private final double[][] testX;
    private final double[] testY;

    public Fold(double[][] trainX, double[] trainY, double[][] testX, double[] testY) {
        this.trainX = trainX;
        this.trainY = trainY;
        this.testX = testX;
        this.testY = testY;
    }

    public static Fold fromIndexes(double[][] X, double[] y, int[] trainIndex, int[] testIndex) {
        int trainSize = trainIndex.length;

        double[][] trainXres = new double[trainSize][];
        double[] trainYres = new double[trainSize];
        for (int i = 0; i < trainSize; i++) {
            int idx = trainIndex[i];
            trainXres[i] = X[idx];
            trainYres[i] = y[idx];
        }

        int testSize = testIndex.length;

        double[][] testXres = new double[testSize][];
        double[] testYres = new double[testSize];
        for (int i = 0; i < testSize; i++) {
            int idx = testIndex[i];
            testXres[i] = X[idx];
            testYres[i] = y[idx];
        }

        return new Fold(trainXres, trainYres, testXres, testYres);
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

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
