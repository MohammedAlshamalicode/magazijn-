package com.prularia.magazijn.magazijnplaats;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MagazijnplaatsNietGevondenException extends RuntimeException {
    public MagazijnplaatsNietGevondenException(long magazijnplaatsId) {
        super("Magazijn Id niet gevonden: " + magazijnplaatsId);
        System.out.println("Magazijnplaats: " + magazijnplaatsId);
    }

    public MagazijnplaatsNietGevondenException(String rij, int rek) {
        super("MagazijnPlaats niet gevonden: " + rij + rek);
        System.out.println("Magazijnplaats: " + rij + rek);
    }
}