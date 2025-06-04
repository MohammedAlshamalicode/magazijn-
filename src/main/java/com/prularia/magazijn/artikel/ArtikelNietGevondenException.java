package com.prularia.magazijn.artikel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArtikelNietGevondenException extends RuntimeException {
    public ArtikelNietGevondenException(long artikelId) {
        super("Artikel niet gevonden : " + artikelId);
    }

    public ArtikelNietGevondenException(String ean) {
        super("Artikel niet gevonden. ean: " + ean);
    }
}
