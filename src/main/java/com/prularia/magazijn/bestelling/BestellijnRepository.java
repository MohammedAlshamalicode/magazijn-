package com.prularia.magazijn.bestelling;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BestellijnRepository {
    private final JdbcClient jdbcClient;

    public BestellijnRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<BestellijnDTO> findByBestelId(long bestelId) {
        var sql = """
                  select bestellijnen.artikelId, naam, beschrijving, aantalBesteld - aantalGeannuleerd as aantal
                  from bestellijnen
                  inner join artikelen on artikelen.artikelId = bestellijnen.artikelId
                  where bestelId = ? and aantalBesteld > aantalGeannuleerd
                  """;
        return jdbcClient.sql(sql).param(bestelId).query(BestellijnDTO.class).list();
    }

    public List<BestellijnMetArtikel> findBestellijnByBestelId(long bestelId) {
        var sql = """
            SELECT b.bestellijnId, b.bestelId, b.artikelId, m.aantal  -- إضافة `m.aantal`
            FROM bestellijnen b
            INNER JOIN artikelen a ON a.artikelId = b.artikelId
            INNER JOIN magazijnplaatsen m ON a.artikelId = m.artikelId
            WHERE b.bestelId = ?
            """;
        return jdbcClient.sql(sql).param(bestelId).query(BestellijnMetArtikel.class).list();
    }

}
