package com.alexeygrigorev.dstools.opt;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.junit.Test;

public class DomainsTest {

    @Test
    public void uniform() {
        Random random = new Random(10);
        Domain<Double> uniform = Domains.uniform(-2, -1);

        double[] values = IntStream.range(0, 1000).mapToDouble(i -> uniform.sample(random)).toArray();

        double min = DoubleStream.of(values).min().getAsDouble();
        assertTrue(min >= -2);

        double max = DoubleStream.of(values).max().getAsDouble();
        assertTrue(max <= -1);
    }

    @Test
    public void linSpace() {
        IterableDomain<Double> uniform = Domains.linSpace(0.0, 1.0, 5);
        List<Double> expected = Arrays.asList(0.0, 0.25, 0.5, 0.75, 1.0);
        assertEquals(expected, uniform.list());
    }

}
