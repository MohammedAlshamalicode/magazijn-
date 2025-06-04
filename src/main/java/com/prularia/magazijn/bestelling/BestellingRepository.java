package com.prularia.magazijn.bestelling;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BestellingRepository {
    private final JdbcClient jdbcClient;

    public BestellingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    //eerste volgende bestelling zoeken
    public Optional<Long> findBestelling() {
        var sql = """
            
                SELECT bestelId
            FROM bestellingen
            WHERE bestellingsStatusId in (2, 4)-- Alleen bestellingen met status 'Betaald' en 'Klaarmaken'
            ORDER BY bestelDatum ASC
            LIMIT 1
            """;
        return jdbcClient.sql(sql).query(Long.class).optional();
    }


    public void updateStatusToKlaarmaken(long bestelId) {
        var sql = """
                UPDATE bestellingen
                SET bestellingsStatusId = 4  -- status verandert naar 'klaarmaken(4)'
                WHERE bestelId = ?
                """;
        jdbcClient.sql(sql)
                .param(bestelId)
                .update();
    }

    public long findKlantId(long bestelId) {
        var sql = """
                select klantId
                from bestellingen
                Where bestelId = ?
                """;
        return jdbcClient.sql(sql).param(bestelId).query(Long.class).single();
    }

    public Optional<Long> findBestellingByBestelIdEnFilterByBestellingStatusId9(long bestelId) {
        var sql = """
                select bestelId
                from bestellingen
                where bestelId = ? and bestellingsStatusId = 9 -- nakijken of het bestaat en of het om retour gaat (bestelStatusId 9 = retour)
                """;
        return jdbcClient.sql(sql).param(bestelId).query(Long.class).optional();
    }

    public int getAantalBestellingen() {
        var sql = """
                  select count(*)
                  from bestellingen
                  where bestellingsStatusId in (2, 4)
                  """;
        return jdbcClient.sql(sql).query(Integer.class).single();
    }

    public List<BestellingDTO> getBestellingen() {
        var sql = """
				select bestellingen.bestelId,
					sum(bestellijnen.aantalBesteld) as aantalProducten,
					sum(bestellijnen.aantalBesteld * artikelen.gewichtInGram)/1000 as totaleGewicht,
					max(bestellingen.besteldatum) as maxDatum
				from bestellingen
				inner join bestellijnen on bestellijnen.bestelId = bestellingen.bestelId
				inner join artikelen on artikelen.artikelId = bestellijnen.artikelId
				where bestellingsStatusId in (2, 4)
				group by bestellijnen.bestelId
				order by maxDatum
				limit 5
				""";
        return jdbcClient.sql(sql).query(BestellingDTO.class).list();
    }
}
