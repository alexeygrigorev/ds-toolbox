package com.alexeygrigorev.dstools.wrappers.jsat;

import jsat.classifiers.Classifier;
import jsat.regression.Regressor;

public class Jsat {

    public static JsatBinaryClassificationWrapper wrap(Classifier model) {
        return new JsatBinaryClassificationWrapper(model);
    }

    public static JsatRegressionWrapper wrap(Regressor model) {
        return new JsatRegressionWrapper(model);
    }

    public static JsatBinaryClassificationWrapper wrapClassifier(Classifier model) {
        return wrap(model);
    }

    public static JsatRegressionWrapper wrapRegression(Regressor model) {
        return wrap(model);
    }
}
