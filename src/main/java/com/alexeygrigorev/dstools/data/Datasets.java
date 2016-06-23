package com.alexeygrigorev.dstools.data;

import java.util.Arrays;

import org.apache.commons.lang3.Validate;

public class Datasets {

    private static final class ArrayDataset implements Dataset {

        private double[][] X;
        private double[] y;

        public ArrayDataset(double[][] X, double[] y) {
            this.X = X;
            this.y = y;
        }

        @Override
        public double[][] getX() {
            return X;
        }

        @Override
        public double[] getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Dataset) {
                Dataset other = (Dataset) obj;
                return areEqual(this, other);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(X) + Arrays.hashCode(y);
        }
    }

    public static Dataset of(double[][] X, double[] y) {
        Validate.notNull(X, "X must not be null");
        Validate.notNull(y, "y must not be null");
        Validate.isTrue(X.length == y.length, "X and y must have the same size");

        return new ArrayDataset(X, y);
    }

    public static Dataset of(double[][] X) {
        Validate.notNull(X, "X must not be null");
        return new ArrayDataset(X, null);
    }

    public static boolean areEqual(Dataset ds1, Dataset ds2) {
        return Arrays.equals(ds1.getX(), ds2.getX()) && Arrays.equals(ds1.getY(), ds2.getY());
    }
}
