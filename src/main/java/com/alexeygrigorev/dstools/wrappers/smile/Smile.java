package com.alexeygrigorev.dstools.wrappers.smile;

import smile.classification.SoftClassifier;
import smile.regression.Regression;

public class Smile {

    public static SmileBinaryClassificationWrapper wrap(SoftClassifier<double[]> model) {
        return new SmileBinaryClassificationWrapper(model);
    }

    public static SmileRegressionWrapper wrap(Regression<double[]> model) {
        return new SmileRegressionWrapper(model);
    }
}
