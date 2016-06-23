package com.alexeygrigorev.dstools.opt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.Validate;

public class Domains {

    private static final Random SHARED_RANDOM = new Random();

    private static class CountiniousDoubleDomain implements Domain<Double> {
        private final double low;
        private final double range;

        public CountiniousDoubleDomain(double low, double high) {
            Validate.isTrue(low < high, "low must be lower than high");
            this.low = low;
            this.range = high - low;
        }

        @Override
        public Double sample() {
            return sample(SHARED_RANDOM);
        }

        @Override
        public Double sample(Random random) {
            double rnd = random.nextDouble();
            return low + rnd * range;
        }
    }

    public static Domain<Double> uniform(double low, double high) {
        return new CountiniousDoubleDomain(low, high);
    }

    public static class DiscreteDomain<E> implements IterableDomain<E> {

        private final List<E> values;

        public DiscreteDomain(List<E> values) {
            this.values = values;
        }

        @Override
        public E sample() {
            return sample(SHARED_RANDOM);
        }

        @Override
        public E sample(Random random) {
            int idx = random.nextInt(values.size());
            return values.get(idx);
        }

        @Override
        public List<E> list() {
            return Collections.unmodifiableList(values);
        }
    }

    public static IterableDomain<Double> linSpace(double from, double to, int steps) {
        List<Double> values = new ArrayList<>(steps);
        double step = (to - from) / (steps - 1);

        double next = from;
        int cnt = steps;
        while (cnt > 0) {
            values.add(next);
            next = next + step;
            cnt--;
        }

        return new DiscreteDomain<>(values);

    }
}
