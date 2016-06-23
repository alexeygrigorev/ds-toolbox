package com.alexeygrigorev.dstools.data;

import java.util.List;

import com.alexeygrigorev.dstools.cv.CV;
import com.alexeygrigorev.dstools.cv.Fold;

public interface Dataset {

    double[][] getX();

    double[] getY();

    default int length() {
        return getX().length;
    }

    default List<Fold> kfold(int k) {
        return CV.kfold(this, k);
    }

    default Fold trainTestSplit(double testRatio) {
        return CV.split(this, testRatio);
    }
}
