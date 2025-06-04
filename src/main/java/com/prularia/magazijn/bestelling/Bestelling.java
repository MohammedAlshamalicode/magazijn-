package com.prularia.magazijn.bestelling;

import java.time.LocalDateTime;

public class Bestelling {
    private final long bestelId;
    private final LocalDateTime bestelDatum;
    private final long klantId;
    private final boolean betaald;
    private final String betalingscode;
    //    private final long betaalwijzeId;
//    private final boolean annulatie;
//    private final String annulatiedatum;
    private final String terugbetalingscode;
    private final long bestellingsStatusId;
    private final boolean actiecodeGebruikt;
    private final String bedrijfsnaam;
    private final String btwNummer;
    private final String voornaam;
    private final String familienaam;
    private final long facturatieAdresId;
    private final long leveringsAdresId;

    public Bestelling(long bestelId, LocalDateTime bestelDatum, long klantId, boolean betaald, String betalingscode, String terugbetalingscode, long bestellingsStatusId, boolean actiecodeGebruikt, String bedrijfsnaam, String btwNummer, String voornaam, String familienaam, long facturatieAdresId, long leveringsAdresId) {
        this.bestelId = bestelId;
        this.bestelDatum = bestelDatum;
        this.klantId = klantId;
        this.betaald = betaald;
        this.betalingscode = betalingscode;
        this.terugbetalingscode = terugbetalingscode;
        this.bestellingsStatusId = bestellingsStatusId;
        this.actiecodeGebruikt = actiecodeGebruikt;
        this.bedrijfsnaam = bedrijfsnaam;
        this.btwNummer = btwNummer;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.facturatieAdresId = facturatieAdresId;
        this.leveringsAdresId = leveringsAdresId;
    }

    public long getBestelId() {
        return bestelId;
    }

    public LocalDateTime getBestelDatum() {
        return bestelDatum;
    }

    public long getKlantId() {
        return klantId;
    }

    public boolean isBetaald() {
        return betaald;
    }

    public String getBetalingscode() {
        return betalingscode;
    }

    public String getTerugbetalingscode() {
        return terugbetalingscode;
    }

    public long getBestellingsStatusId() {
        return bestellingsStatusId;
    }

    public boolean isActiecodeGebruikt() {
        return actiecodeGebruikt;
    }

    public String getBedrijfsnaam() {
        return bedrijfsnaam;
    }

    public String getBtwNummer() {
        return btwNummer;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public long getFacturatieAdresId() {
        return facturatieAdresId;
    }

    public long getLeveringsAdresId() {
        return leveringsAdresId;
    }
}
