package com.alexeygrigorev.dstools.wrappers.libsvm;

import org.apache.commons.lang3.Validate;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.RegressionModel;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class LibSvmRegressionWrapper implements RegressionModel {

    private final svm_parameter param;
    private svm_model model;

    public LibSvmRegressionWrapper(svm_parameter param) {
        this.param = param;
    }

    @Override
    public void fit(Dataset dataset) {
        svm_problem problem = LibSVM.wrapDataset(dataset);
        this.model = svm.svm_train(problem, param);
    }

    @Override
    public double[] predict(Dataset dataset) {
        Validate.validState(model != null, "call fit before calling predict");
        return LibSVM.predict(model, dataset);
    }

}
