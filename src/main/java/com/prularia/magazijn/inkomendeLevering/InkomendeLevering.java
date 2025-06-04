package com.prularia.magazijn.inkomendeLevering;

import java.time.LocalDate;

public class InkomendeLevering {
    private final long inkomendeLeveringId;
    private final long leveranciersId;
    private final String leveringsbonNummer;
    private final LocalDate leveringsbondatum;
    private final LocalDate leverDatum;
    private final long ontvangerPersoneelslidId;

    public InkomendeLevering(long inkomendeLeveringId, long leveranciersId, String leveringsbonNummer, LocalDate leveringsbondatum, LocalDate leverDatum, long ontvangerPersoneelslidId) {
        this.inkomendeLeveringId = inkomendeLeveringId;
        this.leveranciersId = leveranciersId;
        this.leveringsbonNummer = leveringsbonNummer;
        this.leveringsbondatum = leveringsbondatum;
        this.leverDatum = leverDatum;
        this.ontvangerPersoneelslidId = ontvangerPersoneelslidId;
    }

    public long getInkomendeLeveringId() {
        return inkomendeLeveringId;
    }

    public long getOntvangerPersoneelslidId() {
        return ontvangerPersoneelslidId;
    }

    public LocalDate getLeverDatum() {
        return leverDatum;
    }

    public LocalDate getLeveringsbondatum() {
        return leveringsbondatum;
    }

    public String getLeveringsbonNummer() {
        return leveringsbonNummer;
    }

    public long getLeveranciersId() {
        return leveranciersId;
    }
}
