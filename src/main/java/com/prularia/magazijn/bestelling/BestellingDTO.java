package com.prularia.magazijn.bestelling;

import java.math.BigDecimal;

public record BestellingDTO(
        long bestelId,
        int aantalProducten,
        BigDecimal totaleGewicht
) {
}
