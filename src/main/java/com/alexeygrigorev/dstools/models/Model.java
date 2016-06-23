package com.alexeygrigorev.dstools.models;

import java.util.Map;
import java.util.Map.Entry;

import com.alexeygrigorev.dstools.data.Dataset;
import com.alexeygrigorev.dstools.opt.Params;

public interface Model {

    <E> void setParam(String name, E value);

    void fit(Dataset dataset);

    default void setParams(Params params) {
        Map<String, Object> paramsMap = params.getParams();
        for (Entry<String, Object> e : paramsMap.entrySet()) {
            setParam(e.getKey(), e.getValue());
        }
    }

}
