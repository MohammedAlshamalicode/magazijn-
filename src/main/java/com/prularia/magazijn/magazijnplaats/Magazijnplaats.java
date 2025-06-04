package com.prularia.magazijn.magazijnplaats;

public class Magazijnplaats {
    private final long magazijnPlaatsId;
    private final long artikelId;
    private final String rij;
    private final long rek;
    private final long aantal;

    public Magazijnplaats(long magazijnPlaatsId, long artikelId, String rij, long rek, long aantal) {
        this.magazijnPlaatsId = magazijnPlaatsId;
        this.artikelId = artikelId;
        this.rij = rij;
        this.rek = rek;
        this.aantal = aantal;
    }

    public long getMagazijnPlaatsId() {
        return magazijnPlaatsId;
    }

    public long getArtikelId() {
        return artikelId;
    }

    public String getRij() {
        return rij;
    }

    public long getRek() {
        return rek;
    }

    public long getAantal() {
        return aantal;
    }

    @Override
    public String toString() {
        return "Plaats: " + rij + rek + ", Aantal: " + aantal;
    }
}
