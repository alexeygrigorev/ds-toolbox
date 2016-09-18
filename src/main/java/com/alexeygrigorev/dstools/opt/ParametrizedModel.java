package com.alexeygrigorev.dstools.opt;

import java.util.Map;
import java.util.Map.Entry;

import com.alexeygrigorev.dstools.models.Model;

public interface ParametrizedModel extends Model {

    <E> void setParam(String name, E value);

    default void setParams(Params params) {
        Map<String, Object> paramsMap = params.getParams();
        for (Entry<String, Object> e : paramsMap.entrySet()) {
            setParam(e.getKey(), e.getValue());
        }
    }

}
