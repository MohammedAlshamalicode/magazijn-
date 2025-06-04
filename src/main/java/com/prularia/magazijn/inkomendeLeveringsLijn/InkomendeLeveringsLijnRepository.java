package com.prularia.magazijn.inkomendeLeveringsLijn;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InkomendeLeveringsLijnRepository {
    private final JdbcClient jdbcClient;

    public InkomendeLeveringsLijnRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public int createInkomendeLeveringsLijn(InkomendeLeveringsLijn inkomendeLeveringsLijn) {
        var sql = """
                INSERT INTO inkomendeLeveringsLijnen (inkomendeLeveringsId, artikelId, aantalGoedgekeurd, aantalTeruggestuurd, magazijnPlaatsId)
                VALUES (?, ?, ?, ?, ?);
                """;
        return jdbcClient.sql(sql).params(inkomendeLeveringsLijn.getInkomendeLeveringsId(), inkomendeLeveringsLijn.getArtikelId(), inkomendeLeveringsLijn.getAantalGoedgekeurd(), inkomendeLeveringsLijn.getAantalTeruggestuurd(), inkomendeLeveringsLijn.getMagazijnPlaatsId()).update();
    }

    public List<InkomendeLeveringsLijnDTO> getLeveringslijnenSortedByMagazijnplaatsId(long inkomendeLeveringsId) {
        var sql = """
                   select il.inkomendeLeveringsId, il.artikelId,ar.naam,ar.beschrijving, il.aantalGoedgekeurd, il.aantalTeruggestuurd, il.magazijnPlaatsId,
                           concat(mp.rij, mp.rek) as plaats
                    from inkomendeleveringslijnen il
                    join artikelen ar on il.artikelId = ar.artikelId
                    join magazijnplaatsen mp on il.magazijnPlaatsId = mp.magazijnPlaatsId
                    where inkomendeLeveringsId = ?
                    order by il.magazijnPlaatsId
                """;
        return jdbcClient.sql(sql)
                .param(inkomendeLeveringsId)
                .query(InkomendeLeveringsLijnDTO.class)
                .list();
    }


}
