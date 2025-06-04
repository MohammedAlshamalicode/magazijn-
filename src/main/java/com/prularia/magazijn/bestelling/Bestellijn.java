package com.prularia.magazijn.bestelling;

import com.prularia.magazijn.LeveringsLijnDTO;

public class Bestellijn implements LeveringsLijnDTO {
    private final long bestellijnId;
    private final long bestelId;
    private final long artikelId;
    private long aantalBesteld;
    private final long aantalGeannuleerd;

    public Bestellijn(long bestellijnId, long bestelId, long artikelId, long aantalBesteld, long aantalGeannuleerd) {
        this.bestellijnId = bestellijnId;
        this.bestelId = bestelId;
        this.artikelId = artikelId;
        this.aantalBesteld = aantalBesteld;
        this.aantalGeannuleerd = aantalGeannuleerd;
    }

    public long getBestellijnId() {
        return bestellijnId;
    }

    public long getBestelId() {
        return bestelId;
    }

    @Override
    public long getLeveringId() {
        return bestelId;
    }
    @Override
    public long getArtikelId() {
        return artikelId;
    }

    @Override
    public long getAantal() {
        return aantalBesteld;
    }

    public long getAantalBesteld() {
        return aantalBesteld;
    }

    public long getAantalGeannuleerd() {
        return aantalGeannuleerd;
    }

    public void setAantalBesteld(long aantalBesteld) {
        this.aantalBesteld = aantalBesteld;
    }
}
