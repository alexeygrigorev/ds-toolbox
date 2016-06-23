package com.alexeygrigorev.dstools.cv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.commons.lang3.Validate;

import com.alexeygrigorev.dstools.data.Dataset;

public class CV {

    public static Fold shuffleSplit(Dataset dataset, double testRatio) {
        return trainTestSplit(dataset, testRatio, true, System.currentTimeMillis());
    }

    public static Fold shuffleSplit(Dataset dataset, double testRatio, long seed) {
        return trainTestSplit(dataset, testRatio, true, seed);
    }

    public static Fold split(Dataset dataset, double testRatio) {
        return trainTestSplit(dataset, testRatio, false, 0);
    }

    public static List<Fold> kfold(Dataset dataset, int k) {
        return kfold(dataset, k, false, 0);
    }

    public static List<Fold> shuffledKfold(Dataset dataset, int k, long seed) {
        return kfold(dataset, k, true, seed);
    }

    public static Fold trainTestSplit(Dataset dataset, double testRatio, boolean shuffle, long seed) {
        Validate.isTrue(testRatio > 0.0 && testRatio < 1.0, "testRatio must be in (0, 1) interval");

        int[] indexes = IntStream.range(0, dataset.length()).toArray();
        if (shuffle) {
            shuffle(indexes, seed);
        }

        int trainSize = (int) (indexes.length * (1 - testRatio));

        int[] trainIndex = Arrays.copyOfRange(indexes, 0, trainSize);
        int[] testIndex = Arrays.copyOfRange(indexes, trainSize, indexes.length);

        return Fold.fromIndexes(dataset, trainIndex, testIndex);
    }

    public static List<Fold> repeatedShuffledSplit(Dataset dataset, int k, double testRatio, long seed) {
        List<Fold> result = new ArrayList<>(k);

        for (int i = 0; i < k; i++) {
            Fold fold = shuffleSplit(dataset, testRatio, seed + i);
            result.add(fold);
        }

        return result;
    }

    public static List<Fold> kfold(Dataset dataset, int k, boolean shuffle, long seed) {
        int length = dataset.length();
        Validate.isTrue(k < length);

        int[] indexes = IntStream.range(0, length).toArray();
        if (shuffle) {
            shuffle(indexes, seed);
        }

        int[][] folds = prepareFolds(indexes, k);
        List<Fold> result = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            int[] testIdx = folds[i];
            int[] trainIdx = combineTrainFolds(folds, indexes.length, i);
            result.add(Fold.fromIndexes(dataset, trainIdx, testIdx));
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
