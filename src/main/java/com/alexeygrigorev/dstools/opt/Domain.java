package com.alexeygrigorev.dstools.opt;

import java.util.Random;

public interface Domain<E> {

    E sample();

    E sample(Random random);

}
