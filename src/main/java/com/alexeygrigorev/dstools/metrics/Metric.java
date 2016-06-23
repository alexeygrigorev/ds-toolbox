package com.alexeygrigorev.dstools.metrics;

public interface Metric {

    double calculate(double[] actual, double[] predicted);

}
