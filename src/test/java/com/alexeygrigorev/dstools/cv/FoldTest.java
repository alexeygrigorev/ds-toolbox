package com.alexeygrigorev.dstools.cv;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class FoldTest {

    @Test
    public void fromIndexes() {
        double[][] X = { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9, 10 } };
        double[] y = { 1, 2, 3, 4, 5 };

        int[] trainIdx = { 0, 2, 4 };
        int[] testIdx = { 1, 3 };

        Fold fold = Fold.fromIndexes(X, y, trainIdx, testIdx);

        double[][] expectedTrainX = { X[0], X[2], X[4] };
        assertTrue(Arrays.equals(expectedTrainX, fold.getTrainX()));

        double[] expectedTrainY = { y[0], y[2], y[4] };
        assertTrue(Arrays.equals(expectedTrainY, fold.getTrainY()));

        double[][] expectedTestX = { X[1], X[3] };
        assertTrue(Arrays.equals(expectedTestX, fold.getTestX()));

        double[] expectedTestY = { y[1], y[3] };
        assertTrue(Arrays.equals(expectedTestY, fold.getTestY()));
    }
}
