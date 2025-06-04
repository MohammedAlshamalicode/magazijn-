package com.prularia.magazijn.artikel;

public class Artikel {
    private final long artikelId;
    private final String ean;
    private final String naam;
    private final String beschrijving;
    //    private BigDecimal prijs;
    private final long gewichtInGram;
    //    private final long bestelpeil;
    private final long voorraad;
    //    private final long minimumVoorraad;
//    private final long maximumVoorraad;
    private final short levertijd;
    private final int aantalBesteldLeverancier;
    private final long maxAantalInMagazijnPLaats;
    private final long leveranciersId;

    public Artikel(long artikelId, String ean, String naam, String beschrijving, long gewichtInGram, long voorraad, short levertijd, int aantalBesteldLeverancier, long maxAantalInMagazijnPLaats, long leveranciersId) {
        this.artikelId = artikelId;
        this.ean = ean;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.gewichtInGram = gewichtInGram;
        this.voorraad = voorraad;
        this.levertijd = levertijd;
        this.aantalBesteldLeverancier = aantalBesteldLeverancier;
        this.maxAantalInMagazijnPLaats = maxAantalInMagazijnPLaats;
        this.leveranciersId = leveranciersId;
    }

    public long getArtikelId() {
        return artikelId;
    }

    public String getEan() {
        return ean;
    }

    public String getNaam() {
        return naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public long getGewichtInGram() {
        return gewichtInGram;
    }

    public long getVoorraad() {
        return voorraad;
    }

    public short getLevertijd() {
        return levertijd;
    }

    public int getAantalBesteldLeverancier() {
        return aantalBesteldLeverancier;
    }

    public long getMaxAantalInMagazijnPLaats() {
        return maxAantalInMagazijnPLaats;
    }

    public long getLeveranciersId() {
        return leveranciersId;
    }
}
