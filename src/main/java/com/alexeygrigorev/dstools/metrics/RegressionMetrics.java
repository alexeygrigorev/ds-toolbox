package com.alexeygrigorev.dstools.metrics;

import org.apache.commons.lang3.Validate;

public class RegressionMetrics {

    public static Metric RMSE = RegressionMetrics::rmse;
    public static Metric MAPE = RegressionMetrics::mape;

    /**
     * Root mean squared error
     * 
     * @param actual
     * @param predicted
     */
    public static double rmse(double[] actual, double[] predicted) {
        validateLength(actual, predicted);
        int n = actual.length;

        double rss = 0.0;
        for (int i = 0; i < n; i++) {
            double diff = actual[i] - predicted[i];
            rss = rss + diff * diff;
        }

        double mse = rss / n;
        return Math.sqrt(mse);
    }

    /**
     * Mean absolute percentage error
     * 
     * https://en.wikipedia.org/wiki/Mean_absolute_percentage_error
     * 
     * @param actual
     * @param predicted
     */
    public static double mape(double[] actual, double[] predicted) {
        validateLength(actual, predicted);
        int n = actual.length;

        double ape = 0.0;
        for (int i = 0; i < n; i++) {
            double diff = Math.abs(actual[i] - predicted[i]);
            ape = ape + diff / actual[i];
        }

        return ape / n;
    }

    private static void validateLength(double[] actual, double[] predicted) {
        Validate.notNull(actual, "actual must not be null");
        Validate.notNull(predicted, "predicted must not be null");
        Validate.isTrue(actual.length == predicted.length, "actual and predicted must have the same length");
    }
}
