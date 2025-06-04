package com.prularia.magazijn;

import com.prularia.magazijn.magazijnplaats.Magazijnplaats;

@FunctionalInterface
public interface BestelLijnCreator<T, R> {
    R create(T dto, long aantal, Magazijnplaats plaats);
}

