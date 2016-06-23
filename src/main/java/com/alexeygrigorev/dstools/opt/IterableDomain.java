package com.alexeygrigorev.dstools.opt;

import java.util.List;

public interface IterableDomain<E> extends Domain<E> {

    List<E> list();

}
