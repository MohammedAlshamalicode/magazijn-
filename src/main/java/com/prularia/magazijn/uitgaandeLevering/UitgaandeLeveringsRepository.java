package com.prularia.magazijn.uitgaandeLevering;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UitgaandeLeveringsRepository {
    private final JdbcClient jdbcClient;

    public UitgaandeLeveringsRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public long createUitgaandeLevering(UitgaandeLevering uitgaandeLevering) {
        var sql = """
                insert into uitgaandeleveringen (bestelId, vertrekDatum, klantId, uitgaandeLeveringsStatusId)
                values (?, ?, ?, ?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql).params(uitgaandeLevering.getBestelId(), uitgaandeLevering.getVertrekDatum(), uitgaandeLevering.getKlantId(), uitgaandeLevering.getUitgaandeLeveringsStatusId()).update(keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Optional<Long> findUitgaandeLeveringsStatusId(String naam) {
        var sql = """
                SELECT uitgaandeLeveringsStatusId 
                FROM uitgaandeleveringsstatussen 
                WHERE naam = ?
                """;
        return jdbcClient.sql(sql).param(naam).query(Long.class).optional();
    }

    public void updateUitgaandeLeveringToRetourInStock(long uitgaandeLeveringsId) {
        var statusId = findUitgaandeLeveringsStatusId("RetourInStock")
                .orElseThrow(() -> new IllegalStateException("Status 'RetourInStock' not found in uitgaandeLeveringStatussen"));

        var sql = """
                UPDATE uitgaandeleveringen
                SET uitgaandeLeveringsStatusId = ?
                WHERE uitgaandeLeveringsId = ?
                """;
        jdbcClient.sql(sql).params(statusId, uitgaandeLeveringsId).update();

        var sqlUpdateBestelling = """
                UPDATE bestellingen 
                SET bestellingsStatusId = (SELECT bestellingsStatusId FROM bestellingsstatussen WHERE naam = 'RetourInStock') 
                WHERE bestelId = (SELECT bestelId FROM uitgaandeleveringen WHERE uitgaandeLeveringsId = ?)
                """;
        jdbcClient.sql(sqlUpdateBestelling).param(uitgaandeLeveringsId).update();
    }

    public boolean isStatus6(long bestelId) {
        var sql = """
        SELECT COUNT(*)
        FROM uitgaandeLeveringen
        WHERE bestelId = ? AND uitgaandeLeveringsStatusId = 6
    """;
        return jdbcClient.sql(sql)
                .param(bestelId)
                .query(Integer.class)
                .single() > 0;
    }
    public boolean isStatus4(long bestelId) {
        var sql = """
        SELECT COUNT(*)
        FROM uitgaandeLeveringen
        WHERE bestelId = ? AND uitgaandeLeveringsStatusId = 4
    """;
        return jdbcClient.sql(sql)
                .param(bestelId)
                .query(Integer.class)
                .single() > 0;
    }


    public void updateStatusToBeschadigd(long bestelId) {
        var sql= """
                UPDATE uitgaandeleveringen
                SET uitgaandeLeveringsStatusId = 4  -- status verandert naar 'Beschadigd(4)'
                WHERE bestelId = ?
                """;
        jdbcClient.sql(sql)
                .param(bestelId)
                .update();
    }
}
