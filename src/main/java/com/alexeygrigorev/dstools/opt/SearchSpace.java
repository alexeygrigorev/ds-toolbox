package com.alexeygrigorev.dstools.opt;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

public class SearchSpace {

    private final Map<String, Domain<?>> paramSpace = Maps.newHashMap();

    public <E> void addParam(String paramName, Domain<E> domain) {
        paramSpace.put(paramName, domain);
    }

    public <E> Domain<E> getParam(String paramName, Class<E> cls) {
        Domain<?> domain = paramSpace.get(paramName);
        @SuppressWarnings("unchecked")
        Domain<E> result = (Domain<E>) domain;
        return result;
    }

    public Map<String, Domain<?>> allParams() {
        return Collections.unmodifiableMap(paramSpace);
    }

}
