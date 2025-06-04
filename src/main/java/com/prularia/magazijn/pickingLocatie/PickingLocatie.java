package com.prularia.magazijn.pickingLocatie;

public class PickingLocatie {
    private final long artikelId;
    private final String artikelNaam;
    private final long magazijnPlaatsId;
    private final char rij;
    private final int rek;
    private long voorraadInPlaats;
    private long aantalBesteld;
    private final String beschrijving;

    public PickingLocatie(long artikelId, String artikelNaam, long magazijnPlaatsId, char rij, int rek, long voorraadInPlaats, long aantalBesteld, String beschrijving) {
        this.artikelId = artikelId;
        this.artikelNaam = artikelNaam;
        this.magazijnPlaatsId = magazijnPlaatsId;
        this.rij = rij;
        this.rek = rek;
        this.voorraadInPlaats = voorraadInPlaats;
        this.aantalBesteld = aantalBesteld;
        this.beschrijving = beschrijving;
    }


    public long getArtikelId() {
        return artikelId;
    }

    public String getArtikelNaam() {
        return artikelNaam;
    }

    public long getMagazijnPlaatsId() {
        return magazijnPlaatsId;
    }

    public char getRij() {
        return rij;
    }

    public int getRek() {
        return rek;
    }

    public long getVoorraadInPlaats() {
        return voorraadInPlaats;
    }

    public long getAantalBesteld() {
        return aantalBesteld;
    }

    public String getBeschrijving() {
        return beschrijving;
    }
}
