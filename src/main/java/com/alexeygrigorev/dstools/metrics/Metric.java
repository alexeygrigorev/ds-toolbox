package com.alexeygrigorev.dstools.metrics;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.Model;

public interface Metric {

    double calculate(double[] actual, double[] predicted);

    default double evaluate(Model model, Dataset dataset) {
        double[] actual = dataset.getY();
        double[] predict = model.predict(dataset);
        return calculate(actual, predict);
    }

}
