package com.prularia.magazijn.bestelling;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql("/bestelling.sql")
@AutoConfigureMockMvc
class BestellijnControllerTest {
    private final static String BESTELLIJNEN_TABLE = "bestellijnen";
    private final MockMvc mockMvc;
    private final JdbcClient jdbcClient;

    public BestellijnControllerTest(MockMvc mockMvc, JdbcClient jdbcClient) {
        this.mockMvc = mockMvc;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void getBestellijnenVindtHetJuisteAantalBestellijnen() throws Exception {
        var id = idVanTestBestelling();
        mockMvc.perform(get("/bestelling/{id}", id)).andExpectAll(
                status().isOk(),
                jsonPath("length()").value(JdbcTestUtils.countRowsInTableWhere(jdbcClient, BESTELLIJNEN_TABLE,
                        "bestelId = " + id)));
    }

    private long idVanTestBestelling() {
        return jdbcClient.sql("select bestelId from bestellingen where voornaam = 'Test Voornaam'").query(Long.class).single();
    }
}