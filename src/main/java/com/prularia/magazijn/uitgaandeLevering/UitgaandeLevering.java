package com.prularia.magazijn.uitgaandeLevering;

import java.time.LocalDate;

public class UitgaandeLevering {
    private final long uitgaandeLeveringsId;
    private final long bestelId;
    private final LocalDate vertrekDatum;
    private final long klantId;
    private long uitgaandeLeveringsStatusId;

    public UitgaandeLevering(long uitgaandeLeveringsId, long bestelId, LocalDate vertrekDatum, long klantId, long uitgaandeLeveringsStatusId) {
        this.uitgaandeLeveringsId = uitgaandeLeveringsId;
        this.bestelId = bestelId;
        this.vertrekDatum = vertrekDatum;
        this.klantId = klantId;
        this.uitgaandeLeveringsStatusId = uitgaandeLeveringsStatusId;
    }

    public long getUitgaandeLeveringsId() {
        return uitgaandeLeveringsId;
    }

    public long getBestelId() {
        return bestelId;
    }

    public LocalDate getVertrekDatum() {
        return vertrekDatum;
    }

    public long getKlantId() {
        return klantId;
    }

    public long getUitgaandeLeveringsStatusId() {
        return uitgaandeLeveringsStatusId;
    }
}
