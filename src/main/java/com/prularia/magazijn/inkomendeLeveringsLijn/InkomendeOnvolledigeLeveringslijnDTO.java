package com.prularia.magazijn.inkomendeLeveringsLijn;

import com.prularia.magazijn.LeveringsLijnDTO;

public record InkomendeOnvolledigeLeveringslijnDTO(
        long leveringId, //inkomendeLeveringsId
        long artikelId,
        long aantalGoedgekeurd,
        long aantalAfgekeurd
) implements LeveringsLijnDTO {
    @Override
    public String toString() {
        return "LeveringId: " + leveringId + ", artikelId: " + artikelId + ", aantalGoedgekeurd: " + aantalGoedgekeurd +
                ", aantalAfgekeurd: " + aantalAfgekeurd;
    }

    @Override
    public long getLeveringId() {
        return leveringId;
    }

    @Override
    public long getArtikelId() {
        return artikelId;
    }

    @Override
    public long getAantal() {
        return aantalGoedgekeurd;
    }
}
