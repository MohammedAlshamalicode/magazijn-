package com.prularia.magazijn.bestelling;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql("/testBestelling.sql")
@AutoConfigureMockMvc
class BestellingControllerTest {
    private final MockMvc mockMvc;
    private final JdbcClient jdbcClient;
    private final Path testResources = Path.of("src/test/resources");

    public BestellingControllerTest(MockMvc mockMvc, JdbcClient jdbcClient) {
        this.mockMvc = mockMvc;
        this.jdbcClient = jdbcClient;
    }

    private long idVanTestBestelling() {
        return jdbcClient.sql("select bestelId from bestellingen where voornaam = 'Test Voornaam'").query(Long.class).single();
    }

    @Test
    void eenGoedePostVoorBestellingAfgerondWerkt() throws Exception {
        var json = Files.readString(testResources.resolve("goedeBestellingAfgerond.json"));
        mockMvc.perform(post("/bestellingen/{id}", idVanTestBestelling())
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

     private long idVanEerstVolgendeBestellingMetStatusVier() {
            return jdbcClient.sql("SELECT bestelId FROM bestellingen WHERE bestellingsStatusId = 4 LIMIT 1")
                    .query(Long.class).single();
        }


    @Test
    void findNextBestellingWerkt() throws Exception {
        var id = idVanEerstVolgendeBestellingMetStatusVier();

        mockMvc.perform(get("/bestellingen/findBestelling"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").value(id)
                );
}
}

