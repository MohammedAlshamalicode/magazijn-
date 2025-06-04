package com.prularia.magazijn.magazijnplaats;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AanvullenInRekDTO(@NotNull @Positive Long artikelId,
                                @NotNull @Positive Long magazijnPlaatsId,
                                @NotNull @Positive Integer aantalGoedgekeurd) {
}
