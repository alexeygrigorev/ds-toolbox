package com.alexeygrigorev.dstools.metrics;

import java.util.Arrays;

import org.apache.commons.lang3.Validate;


public class ClassificationMetrics {

    public static Metric AUC = ClassificationMetrics::auc;

    public static double auc(double[] actual, double[] predicted) {
        Validate.isTrue(actual.length == predicted.length, "the lengths don't match");

        int[] truth = Arrays.stream(actual).mapToInt(i -> (int) i).toArray();
        double auc = smile.validation.AUC.measure(truth, predicted);
        if (auc > 0.5) {
            return auc;
        } else {
            return 1 - auc;
        }
    }
}
