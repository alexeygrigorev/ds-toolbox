package com.alexeygrigorev.dstools.cv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

public class CVTest {

    @Test
    public void shuffle() {
        int[] indexes = IntStream.rangeClosed(0, 10).toArray();
        int[] original = indexes.clone();
        long seed = 1;
        CV.shuffle(indexes, seed);

        assertFalse(Arrays.equals(indexes, original));
    }

    @Test
    public void split_lengths() {
        double[][] X = { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9, 10 } };
        double[] y = { 1, 2, 3, 4, 5 };
        double testRatio = 0.1;
        Fold split = CV.split(X, y, testRatio);

        assertEquals(X.length, split.getTestX().length + split.getTrainX().length);
        assertEquals(y.length, split.getTestY().length + split.getTrainY().length);

        assertEquals(1, split.getTestX().length);
        assertEquals(1, split.getTestY().length);
    }

    @Test
    public void shuffleSplit_reproducible() {
        double[][] X = { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9, 10 } };
        double[] y = { 1, 2, 3, 4, 5 };
        double testRatio = 0.1;

        long seed = 1;
        Fold split1 = CV.shuffleSplit(X, y, testRatio, seed);
        Fold split2 = CV.shuffleSplit(X, y, testRatio, seed);

        assertEquals(split1, split2);
    }

    @Test
    public void kfold_2() {
        double[][] X = { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9, 10 } };
        double[] y = { 1, 2, 3, 4, 5 };

        int k = 2;
        List<Fold> folds = CV.kfold(X, y, k);

        double[][] fold1X = { X[0], X[1] };
        double[] fold1Y = { y[0], y[1] };
        double[][] fold2X = { X[2], X[3], X[4] };
        double[] fold2Y = { y[2], y[3], y[4] };

        Fold fold0 = new Fold(fold2X, fold2Y, fold1X, fold1Y);
        assertEquals(fold0, folds.get(0));

        Fold fold1 = new Fold(fold1X, fold1Y, fold2X, fold2Y);
        assertEquals(fold1, folds.get(1));
    }

    @Test
    public void kfold_3() {
        double[][] X = { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9, 10 }, { 11, 12 }, { 13, 14 } };
        double[] y = { 1, 2, 3, 4, 5, 6, 7 };

        int k = 3;
        List<Fold> folds = CV.kfold(X, y, k);

        double[][] trainX1 = { X[2], X[3], X[4], X[5], X[6] };
        double[] trainY1 = { y[2], y[3], y[4], y[5], y[6] };
        double[][] testX1 = { X[0], X[1] };
        double[] testY1 = { y[0], y[1] };

        Fold fold1 = new Fold(trainX1, trainY1, testX1, testY1);
        assertEquals(fold1, folds.get(0));

        double[][] trainX2 = { X[0], X[1], X[4], X[5], X[6] };
        double[] trainY2 = { y[0], y[1], y[4], y[5], y[6] };
        double[][] testX2 = { X[2], X[3] };
        double[] testY2 = { y[2], y[3] };

        Fold fold2 = new Fold(trainX2, trainY2, testX2, testY2);
        assertEquals(fold2, folds.get(1));

        double[][] trainX3 = { X[0], X[1], X[2], X[3] };
        double[] trainY3 = { y[0], y[1], y[2], y[3] };
        double[][] testX3 = { X[4], X[5], X[6] };
        double[] testY3 = { y[4], y[5], y[6] };

        Fold fold3 = new Fold(trainX3, trainY3, testX3, testY3);
        assertEquals(fold3, folds.get(2));
    }

}
