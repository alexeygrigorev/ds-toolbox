package com.alexeygrigorev.dstools.regression;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.models.Model;

public interface RegressionModel extends Model {

    double[] predict(Dataset dataset);

}
