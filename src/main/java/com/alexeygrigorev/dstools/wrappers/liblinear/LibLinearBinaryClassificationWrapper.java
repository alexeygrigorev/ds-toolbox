package com.alexeygrigorev.dstools.wrappers.liblinear;

import org.apache.commons.lang3.Validate;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.BinaryClassificationModel;

import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;

public class LibLinearBinaryClassificationWrapper implements BinaryClassificationModel {

    private final Parameter param;
    private Model model;

    public LibLinearBinaryClassificationWrapper(Parameter param) {
        this.param = param;
    }

    @Override
    public void fit(Dataset dataset) {
        Problem problem = LibLinear.wrapDataset(dataset);
        model = Linear.train(problem, param);
    }

    @Override
    public double[] predict(Dataset dataset) {
        Validate.validState(model != null, "call fit before calling predict");
        return LibLinear.predictProba(model, dataset);
    }

}
