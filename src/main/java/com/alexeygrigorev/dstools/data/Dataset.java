package com.alexeygrigorev.dstools.data;

import java.util.List;

import com.alexeygrigorev.dstools.cv.CV;
import com.alexeygrigorev.dstools.cv.Split;

public interface Dataset {

    double[][] getX();

    double[] getY();

    default int length() {
        return getX().length;
    }

    default List<Split> kfold(int k) {
        return CV.kfold(this, k);
    }

    default Split trainTestSplit(double testRatio) {
        return CV.split(this, testRatio);
    }
}
