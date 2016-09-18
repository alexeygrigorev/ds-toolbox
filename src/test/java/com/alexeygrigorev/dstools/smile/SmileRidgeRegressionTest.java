package com.alexeygrigorev.dstools.smile;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.alexeygrigorev.dstools.data.Datasets;
import com.alexeygrigorev.dstools.wrappers.smile.SmileRidgeRegression;

public class SmileRidgeRegressionTest {

    @Test
    public void passParamsViaConstructor() {
        double[][] X = { { 1, 2 }, { 2, 3 }, { 3, 4 } };
        double[] y = { 1, 2, 4 };

        SmileRidgeRegression reg = new SmileRidgeRegression(1.0);
        reg.fit(Datasets.of(X, y));

        double[] predict = reg.predict(Datasets.of(X));

        assertEquals(1.04, predict[0], 0.01);
        assertEquals(2.33, predict[1], 0.01);
        assertEquals(3.61, predict[2], 0.01);
    }

    @Test
    public void passParamsViaMethod() {
        double[][] X = { { 1, 2 }, { 2, 3 }, { 3, 4 } };
        double[] y = { 1, 2, 4 };

        SmileRidgeRegression reg = new SmileRidgeRegression();
        reg.setParam("lambda", 2.0);
        reg.fit(Datasets.of(X, y));

        double[] predict = reg.predict(Datasets.of(X));

        assertEquals(1.20, predict[0], 0.01);
        assertEquals(2.33, predict[1], 0.01);
        assertEquals(3.45, predict[2], 0.01);
    }

}
