package com.alexeygrigorev.dstools.models;

import com.alexeygrigorev.dstools.data.Dataset;

public interface Model {

    void fit(Dataset dataset);

    double[] predict(Dataset dataset);

}
