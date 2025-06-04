package com.prularia.magazijn.magazijnplaats;

import com.prularia.magazijn.artikel.OnvoldoendeVoorraadException;
import com.prularia.magazijn.pickingLocatie.PickingLocatie;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MagazijnplaatsRepository {
    private final JdbcClient jdbcClient;

    public MagazijnplaatsRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<PickingLocatie> findLocatiesVoorBestelling(long bestelId) {
        var sql = """
                SELECT
                      mp.artikelId, a.naam as artikelNaam, mp.magazijnPlaatsId, mp.rij, mp.rek,
                      mp.aantal AS voorraadInPlaats, bl.aantalBesteld
                FROM magazijnplaatsen mp
                JOIN artikelen a ON mp.artikelId = a.artikelId
                JOIN bestellijnen bl ON bl.artikelId = mp.artikelId
                WHERE bl.bestelId = ?
                ORDER BY bl.artikelId, mp.rij, mp.rek
                """;
        return jdbcClient.sql(sql)
                .param(bestelId)
                .query(PickingLocatie.class)
                .list();
    }

    public int vindBeschikbareVoorraad(long magazijnPlaatsId) {
        var sql = """
                SELECT aantal
                FROM magazijnplaatsen
                WHERE magazijnPlaatsId = ?
                """;
        return jdbcClient.sql(sql)
                .param(magazijnPlaatsId)
                .query(Integer.class)
                .single();
    }

    public Map<String, List<PickingLocatie>> findGroupedByCellAndOrdered(long bestelId) {
        var sql = """
        SELECT
            mp.artikelId, a.naam as artikelNaam, mp.magazijnPlaatsId, mp.rij, mp.rek, mp.aantal AS voorraadInPlaats,
            bl.aantalBesteld, a.beschrijving
        FROM magazijnplaatsen mp
        JOIN artikelen a ON mp.artikelId = a.artikelId
        JOIN bestellijnen bl ON bl.artikelId = mp.artikelId
        WHERE bl.bestelId = ?
        ORDER BY mp.rij, mp.rek, bl.artikelId
    """;
        List<PickingLocatie> locaties = jdbcClient.sql(sql)
                .param(bestelId)
                .query(PickingLocatie.class)
                .list();

        return locaties.stream().collect(Collectors.groupingBy(
                locatie -> locatie.getRij() + "-" + locatie.getRek()
        ));
    }

    public Optional<Long> findIdByPlaats(String rij, int rek) {
        var sql = """
                  select magazijnPlaatsId
                  from magazijnplaatsen
                  where rij = ? and rek = ?
                  """;
        return jdbcClient.sql(sql).params(rij, rek).query(Long.class).optional();
    }

    public void pasMagazijnplaatsAan(long magazijnplaatsId, long aantal) {
        var sql = """
                  update magazijnplaatsen
                  set aantal = aantal - ?
                  where magazijnPlaatsId = ? and aantal >= ?
                  """;
        if (jdbcClient.sql(sql).params(aantal, magazijnplaatsId, aantal).update() == 0) {
            var sqlMagazijnplaatsId = """
                               select aantal
                               from magazijnplaatsen
                               where magazijnPlaatsId = ?
                               """;
            var result = jdbcClient.sql(sqlMagazijnplaatsId).param(magazijnplaatsId).query(Long.class).optional();
            var magazijnplaatsAantal = result.orElseThrow(() -> new MagazijnplaatsNietGevondenException(magazijnplaatsId));
            if (magazijnplaatsAantal < aantal) {
                throw new OnvoldoendeVoorraadException(magazijnplaatsId, aantal);
            }
        }
    }

    public List<MagazijnplaatsDTO> findBeschikbarePlaatsenVoorArtikel(long artikelId) {
        var sql = """
                SELECT mp.magazijnPlaatsId, mp.artikelId, mp.rij, mp.rek, mp.aantal,a.voorraad,
                a.maxAantalInMagazijnPlaats AS maxAantalInMagazijnPlaats
                FROM magazijnplaatsen mp
                LEFT JOIN artikelen a ON mp.artikelId = a.artikelId
                WHERE mp.artikelId = ?
                ORDER BY mp.aantal ASC
                
                """;
        return jdbcClient.sql(sql).param(artikelId).query(MagazijnplaatsDTO.class).list();
    }

    public void verhoogMagazijnplaatsVoorraad(List<MagazijnplaatsDTO> locaties, long aantal) {
        long AantalRemains = aantal;
        for (MagazijnplaatsDTO locatie : locaties) {
            if (AantalRemains <= 0) break;
            var sql = """
            UPDATE magazijnplaatsen 
            SET aantal = aantal + ?
            WHERE magazijnPlaatsId = ? 
            AND artikelId = ?
            AND (aantal + ?) <= (SELECT maxAantalInMagazijnPlaats FROM artikelen WHERE artikelen.artikelId = magazijnplaatsen.artikelId)
            """;
            long beschikbareRuimte = locatie.maxAantalInMagazijnPlaats() - locatie.aantal();
            int toAdd = (int) Math.min(beschikbareRuimte, AantalRemains);
            if (toAdd > 0) {
                int updatedRows = jdbcClient.sql(sql).params(toAdd, locatie.magazijnPlaatsId(), locatie.artikelId(),toAdd).update();
                if (updatedRows > 0) {
                    System.out.println("Voorraad verhoogd met " + toAdd + " op locatie " + locatie.magazijnPlaatsId());
                    AantalRemains -= toAdd;
                }
            }
        }
        if (AantalRemains > 0) {
            System.out.println("Niet genoeg ruimte beschikbaar voor alle producten. Overgebleven aantal: " + AantalRemains);
//            throw new MagazijnplaatsNietGevondenException(maga);
        }
    }

    public void resetLegePlaatsen() {
        var sql = """
                  update magazijnplaatsen
                  set artikelId = null
                  where aantal = 0
                  """;
        jdbcClient.sql(sql).update();
    }

    // Het aantal op een specifieke magazijnplaats aanvullen
    public void aanvullenMagazijnplaats(long magazijnPlaatsId, int aantal) {
        var sql = """
            UPDATE magazijnplaatsen
            SET aantal = aantal + ?
            WHERE magazijnPlaatsId = ?
            """;

        jdbcClient.sql(sql)
                .params(aantal, magazijnPlaatsId)
                .update();
    }

    // Het huidige aantal op een magazijnplaats ophalen
    public int getHuidigAantalOpMagazijnplaats(long magazijnPlaatsId) {
        var sql = """
            SELECT aantal
            FROM magazijnplaatsen
            WHERE magazijnPlaatsId = ?
            """;

        return jdbcClient.sql(sql)
                .param(magazijnPlaatsId)
                .query(int.class)
                .single();
    }

    // De maximale capaciteit van een magazijnplaats ophalen
    public int getMaxAantalInMagazijnplaats(long artikelId) {
        var sql = """
            SELECT maxAantalInMagazijnPlaats
            FROM artikelen
            WHERE artikelId = ?
            """;

        return jdbcClient.sql(sql)
                .param(artikelId)
                .query(int.class)
                .single();
    }

    public List<Magazijnplaats> findPlaatsenByArtikelId(long artikelId) {
        var sql = """
                  select magazijnPlaatsId, artikelId, rij, rek, aantal
                  from magazijnplaatsen
                  where artikelId = ?
                  """;
        return jdbcClient.sql(sql).param(artikelId).query(Magazijnplaats.class).list();
    }

    public int setArtikel(long artikelId) {
        var sql = """
                  update magazijnplaatsen as mp1
                  set mp1.artikelId = ?
                  where mp1.magazijnPlaatsId = (
                      select plaatsId from (
                          select min(mp2.magazijnPlaatsId) as plaatsId
                          from magazijnplaatsen as mp2
                          where mp2.artikelId is null
                      ) as plaatsen
                  )
                  """;
        return jdbcClient.sql(sql).params(artikelId).update();
    }
}

