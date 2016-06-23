package com.alexeygrigorev.dstools.opt;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

public class Params {

    private final Map<String, Object> params = new HashMap<>();

    public Params() {
    }

    public <E> void setParam(String name, E value) {
        params.put(name, value);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public <E> E getParam(String name, Class<E> cls) {
        Validate.isTrue(params.containsKey(name), "param " + name + " is not found. Try these: " + params.keySet());
        @SuppressWarnings("unchecked")
        E result = (E) params.get(name);
        return result;
    }

    public double getDouble(String name) {
        return getParam(name, Double.class);
    }

    @Override
    public String toString() {
        return params.toString();
    }
}
