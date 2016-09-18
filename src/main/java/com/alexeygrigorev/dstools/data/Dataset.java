package com.alexeygrigorev.dstools.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.alexeygrigorev.dstools.cv.CV;
import com.alexeygrigorev.dstools.cv.Split;

public interface Dataset extends Serializable {

    double[][] getX();

    double[] getY();

    default int length() {
        return getX().length;
    }

    default List<Split> kfold(int k) {
        return CV.kfold(this, k);
    }

    default List<Split> shuffleKFold(int k) {
        return CV.shuffledKfold(this, k, 0x100);
    }

    default Split trainTestSplit(double testRatio) {
        return CV.split(this, testRatio);
    }

    default Split shuffleSplit(double testRatio) {
        return CV.shuffleSplit(this, testRatio, 0x100);
    }

    default int[] getYAsInt() {
        return Arrays.stream(getY()).mapToInt(d -> (int) d).toArray();
    }
}
