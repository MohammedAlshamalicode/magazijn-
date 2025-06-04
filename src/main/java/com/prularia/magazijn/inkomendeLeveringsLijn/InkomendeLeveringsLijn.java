package com.prularia.magazijn.inkomendeLeveringsLijn;

public class InkomendeLeveringsLijn {

    private final long inkomendeLeveringsId;
    private final long artikelId;
    private final long aantalGoedgekeurd;
    private final long aantalTeruggestuurd;
    private final long magazijnPlaatsId;

    public InkomendeLeveringsLijn(long inkomendeLeveringsId, long artikelId, long aantalGoedgekeurd, long aantalTeruggestuurd, long magazijnPlaatsId) {
        this.inkomendeLeveringsId = inkomendeLeveringsId;
        this.artikelId = artikelId;
        this.aantalGoedgekeurd = aantalGoedgekeurd;
        this.aantalTeruggestuurd = aantalTeruggestuurd;
        this.magazijnPlaatsId = magazijnPlaatsId;
    }

    public long getInkomendeLeveringsId() {
        return inkomendeLeveringsId;
    }

    public long getAantalGoedgekeurd() {
        return aantalGoedgekeurd;
    }

    public long getArtikelId() {
        return artikelId;
    }

    public long getAantalTeruggestuurd() {
        return aantalTeruggestuurd;
    }

    public long getMagazijnPlaatsId() {
        return magazijnPlaatsId;
    }

    @Override
    public String toString() {
        return "inkomendeLeveringsId: " + inkomendeLeveringsId + ", artikelId: " + artikelId + ", aantalGoedgekeurd: " +
                aantalGoedgekeurd + ", aantalTeruggestuurd: " + aantalTeruggestuurd + ", magazijnPlaatsId: " + magazijnPlaatsId;
    }
}
