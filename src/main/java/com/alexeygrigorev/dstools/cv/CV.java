package com.alexeygrigorev.dstools.cv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.commons.lang3.Validate;

public class CV {

    public static Fold shuffleSplit(double[][] X, double[] y, double testRatio) {
        return trainTestSplit(X, y, testRatio, true, System.currentTimeMillis());
    }

    public static Fold shuffleSplit(double[][] X, double[] y, double testRatio, long seed) {
        return trainTestSplit(X, y, testRatio, true, seed);
    }

    public static Fold split(double[][] X, double[] y, double testRatio) {
        return trainTestSplit(X, y, testRatio, false, 0);
    }

    public static List<Fold> kfold(double[][] X, double[] y, int k) {
        return kfold(X, y, k, false, 0);
    }

    public static List<Fold> shuffledKfold(double[][] X, double[] y, int k, long seed) {
        return kfold(X, y, k, true, seed);
    }

    public static Fold trainTestSplit(double[][] X, double[] y, double testRatio, boolean shuffle, long seed) {
        Validate.notNull(X, "X must not be null");
        Validate.notNull(y, "y must not be null");
        Validate.isTrue(X.length == y.length, "X and y must have the same size");
        Validate.isTrue(testRatio > 0.0 && testRatio < 1.0, "testRatio must be in (0, 1) interval");

        int[] indexes = IntStream.range(0, X.length).toArray();
        if (shuffle) {
            shuffle(indexes, seed);
        }

        int trainSize = (int) (indexes.length * (1 - testRatio));

        int[] trainIndex = Arrays.copyOfRange(indexes, 0, trainSize);
        int[] testIndex = Arrays.copyOfRange(indexes, trainSize, indexes.length);

        return Fold.fromIndexes(X, y, trainIndex, testIndex);
    }

    public static List<Fold> repeatedShuffledSplit(double[][] X, double[] y, int k, double testRatio, long seed) {
        List<Fold> result = new ArrayList<>(k);

        for (int i = 0; i < k; i++) {
            Fold fold = shuffleSplit(X, y, testRatio, seed + i);
            result.add(fold);
        }

        return result;
    }

    public static List<Fold> kfold(double[][] X, double[] y, int k, boolean shuffle, long seed) {
        Validate.notNull(X, "X must not be null");
        Validate.notNull(y, "y must not be null");
        Validate.isTrue(X.length == y.length, "X and y must have the same size");
        Validate.isTrue(k < X.length);

        int[] indexes = IntStream.range(0, X.length).toArray();
        if (shuffle) {
            shuffle(indexes, seed);
        }

        int[][] folds = prepareFolds(indexes, k);
        List<Fold> result = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            int[] testIdx = folds[i];
            int[] trainIdx = combineTrainFolds(folds, indexes.length, i);
            result.add(Fold.fromIndexes(X, y, trainIdx, testIdx));
        }

        return result;
    }

    private static int[] combineTrainFolds(int[][] folds, int totalSize, int excludeIndex) {
        int size = totalSize - folds[excludeIndex].length;
        int result[] = new int[size];

        int start = 0;
        for (int i = 0; i < folds.length; i++) {
            if (i == excludeIndex) {
                continue;
            }
            int[] fold = folds[i];
            System.arraycopy(fold, 0, result, start, fold.length);
            start = start + fold.length;
        }

        return result;
    }

    private static int[][] prepareFolds(int[] indexes, int k) {
        int[][] foldIndexes = new int[k][];

        int step = indexes.length / k;
        int beginIndex = 0;

        for (int i = 0; i < k - 1; i++) {
            foldIndexes[i] = Arrays.copyOfRange(indexes, beginIndex, beginIndex + step);
            beginIndex = beginIndex + step;
        }

        foldIndexes[k - 1] = Arrays.copyOfRange(indexes, beginIndex, indexes.length);
        return foldIndexes;
    }

    /**
     * 
     * Shuffles an array of indexes
     * 
     * Implementation taken from
     * http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
     * 
     * @param indexes
     * @param seed
     */
    public static void shuffle(int[] indexes, long seed) {
        Random rnd = new Random(seed);

        for (int i = indexes.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);

            int tmp = indexes[index];
            indexes[index] = indexes[i];
            indexes[i] = tmp;
        }
    }
}
