package com.prularia.magazijn.artikel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OnvoldoendeVoorraadException extends RuntimeException {
    public OnvoldoendeVoorraadException(long artikelId, long voorraad) {
        super("Onvoldoende voorraad van artikel: " + artikelId + ": " + voorraad);
    }
}
