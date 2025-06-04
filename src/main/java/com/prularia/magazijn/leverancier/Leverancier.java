package com.prularia.magazijn.leverancier;

public class Leverancier {
    private final long leveranciersId;
    private final String naam;
    private final String btwNummer;
    private final String straat;
    private final String huisNummer;
    private final String bus;
    private final long plaatsId;
    private final String familienaamContactpersoon;
    private final String voornaamContactpersoon;

    public Leverancier(long leveranciersId, String naam, String btwNummer, String straat, String huisNummer, String bus, long plaatsId, String familienaamContactpersoon, String voornaamContactpersoon) {
        this.leveranciersId = leveranciersId;
        this.naam = naam;
        this.btwNummer = btwNummer;
        this.straat = straat;
        this.huisNummer = huisNummer;
        this.bus = bus;
        this.plaatsId = plaatsId;
        this.familienaamContactpersoon = familienaamContactpersoon;
        this.voornaamContactpersoon = voornaamContactpersoon;


    }

    public long getLeveranciersId() {
        return leveranciersId;
    }

    public String getNaam() {
        return naam;
    }

    public String getBtwNummer() {
        return btwNummer;
    }

    public String getStraat() {
        return straat;
    }

    public String getHuisNummer() {
        return huisNummer;
    }

    public String getBus() {
        return bus;
    }

    public long getPlaatsId() {
        return plaatsId;
    }

    public String getFamilienaamContactpersoon() {
        return familienaamContactpersoon;
    }

    public String getVoornaamContactpersson() {
        return voornaamContactpersoon;
    }
}
