package com.alexeygrigorev.dstools.linalg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.linear.ArrayRealVector;

import com.google.common.collect.Sets;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.sparse.CompRowMatrix;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;
import smile.data.SparseDataset;
import smile.math.SparseArray;

public class MatrixUtils {

    public static double[] sparseMatrixVectorMult(SparseDataset matrix, SparseArray vector) {
        CompRowMatrix A = smileToMtjMatrix(matrix);
        SparseVector x = smileToMtjVector(matrix.ncols(), vector);

        DenseVector result = new DenseVector(A.numRows());
        A.mult(x, result);
        return result.getData();
    }

    private static SparseVector smileToMtjVector(int ncol, SparseArray vector) {
        int size = vector.size();
        int[] indexes = new int[size];
        double[] values = new double[size];

        Iterator<SparseArray.Entry> iterator = vector.iterator();
        int idx = 0;
        while (iterator.hasNext()) {
            SparseArray.Entry entry = iterator.next();
            indexes[idx] = entry.i;
            values[idx] = entry.x;
            idx++;
        }

        return new SparseVector(ncol, indexes, values, false);
    }

    public static double[] denseMatrixVectorProduct(double[][] matrix, double[] vector) {
        if (matrix.length > 1_000_000) {
            return batchMatrixVectorMult(matrix, vector, 100_000);
        }

        return fullMatrixVectorMult(matrix, vector);
    }

    private static double[] fullMatrixVectorMult(double[][] matrix, double[] vector) {
        DenseMatrix X = new DenseMatrix(matrix);
        DenseVector v = new DenseVector(vector);

        DenseVector result = new DenseVector(X.numRows());
        X.mult(v, result);
        return result.getData();
    }

    public static double[] batchMatrixVectorMult(double[][] matrix, double[] vector, int sliceSize) {
        int size = matrix.length;
        double[] result = new double[size];

        for (int i = 0; i < size; i = i + sliceSize) {
            int end = i + sliceSize;
            if (end > size) {
                end = size;
            }

            double[][] m = Arrays.copyOfRange(matrix, i, end);
            double[] res = fullMatrixVectorMult(m, vector);
            System.arraycopy(res, 0, result, i, res.length);
        }

        return result;
    }

    public static double[][] sparseMatrixMult(SparseDataset d1, SparseDataset d2) {
        CompRowMatrix M1 = smileToMtjMatrix(d1);
        CompRowMatrix M2 = smileToMtjMatrix(d2);

        DenseMatrix M1M2T = new DenseMatrix(M1.numRows(), M2.numRows());
        M1.transBmult(M2, M1M2T);

        return unwrapMtjDenseMatrix(M1M2T);
    }

    private static double[][] unwrapMtjDenseMatrix(DenseMatrix dense) {
        double[] data = dense.getData();
        int nrows = dense.numRows();
        int ncols = dense.numColumns();
        double[][] result = new double[nrows][ncols];

        for (int col = 0; col < ncols; col++) {
            for (int row = 0; row < nrows; row++) {
                result[row][col] = data[row + col * nrows];
            }
        }

        return result;
    }

    public static CompRowMatrix smileToMtjMatrix(SparseDataset dataset) {
        int ncols = dataset.ncols();
        int nrows = dataset.size();
        FlexCompRowMatrix X = new FlexCompRowMatrix(nrows, ncols);

        SparseArray[] array = dataset.toArray(new SparseArray[0]);
        for (int rowIdx = 0; rowIdx < array.length; rowIdx++) {
            Iterator<SparseArray.Entry> row = array[rowIdx].iterator();
            while (row.hasNext()) {
                SparseArray.Entry entry = row.next();
                X.set(rowIdx, entry.i, entry.x);
            }
        }

        return new CompRowMatrix(X);
    }


    public static double[][] sparseDenseMatrixMult(SparseDataset dataset, double[][] base) {
        CompRowMatrix X = smileToMtjMatrix(dataset);
        DenseMatrix V = new DenseMatrix(base);

        DenseMatrix XV = new DenseMatrix(X.numRows(), V.numColumns());
        X.mult(V, XV);

        return unwrapMtjDenseMatrix(XV);
}

    public static double[][] l2RowNormalize(double[][] data) {
        for (int i = 0; i < data.length; i++) {
            double[] row = data[i];
            ArrayRealVector vector = new ArrayRealVector(row, false);
            double norm = vector.getNorm();
            if (norm != 0) {
                vector.mapDivideToSelf(norm);
                data[i] = vector.getDataRef();
            }
        }

        return data;
    }

    public static double[] rowWiseDot(double[][] m1, double[][] m2) {
        Validate.isTrue(m1.length == m2.length);
        Validate.isTrue(m1[0].length == m2[0].length);

        int nrow = m1.length;
        double[] result = new double[nrow];

        for (int i = 0; i < nrow; i++) {
            result[i] = denseDot(m1[i], m2[i]);
        }

        return result;
    }

    private static double denseDot(double[] arr1, double[] arr2) {
        ArrayRealVector v1 = new ArrayRealVector(arr1, false);
        ArrayRealVector v2 = new ArrayRealVector(arr2, false);
        return v1.dotProduct(v2);
    }

    public static double[] rowWiseSparseDot(SparseDataset m1, SparseDataset m2) {
        Validate.isTrue(m1.size() == m2.size(), "the number of rows are diffrent: %d != %d", m1.size(), m2.size());
        Validate.isTrue(m1.ncols() == m2.ncols(), "the number of cols are diffrent: %d != %d", m1.ncols(), m2.ncols());

        int nrow = m1.size();
        double[] result = new double[nrow];

        for (int i = 0; i < nrow; i++) {
            result[i] = sparseDot(m1.get(i).x, m2.get(i).x);
        }

        return result;
    }

    private static double sparseDot(SparseArray x1, SparseArray x2) {
        Map<Integer, Double> row1 = wrapToMap(x1);
        Map<Integer, Double> row2 = wrapToMap(x2);

        double result = 0.0;

        Set<Integer> common = Sets.intersection(row1.keySet(), row2.keySet());
        for (Integer idx : common) {
            result = result + row1.get(idx) * row2.get(idx);
        }

        return result;
    }

    private static Map<Integer, Double> wrapToMap(SparseArray vec) {
        Map<Integer, Double> map = new HashMap<>();
        for (SparseArray.Entry el : vec) {
            map.put(el.i, el.x);
        }
        return map;
    }

}