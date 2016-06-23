package com.alexeygrigorev.dstools.cv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.alexeygrigorev.dstools.data.Datasets;

public class FoldTest {

    @Test
    public void fromIndexes() {
        double[][] X = { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9, 10 } };
        double[] y = { 1, 2, 3, 4, 5 };

        int[] trainIdx = { 0, 2, 4 };
        int[] testIdx = { 1, 3 };

        Fold fold = Fold.fromIndexes(Datasets.of(X, y), trainIdx, testIdx);

        double[][] expectedTrainX = { X[0], X[2], X[4] };
        double[] expectedTrainY = { y[0], y[2], y[4] };
        assertEquals(Datasets.of(expectedTrainX, expectedTrainY), fold.getTrain());

        double[][] expectedTestX = { X[1], X[3] };
        double[] expectedTestY = { y[1], y[3] };
        assertEquals(Datasets.of(expectedTestX, expectedTestY), fold.getTest());
    }
}
