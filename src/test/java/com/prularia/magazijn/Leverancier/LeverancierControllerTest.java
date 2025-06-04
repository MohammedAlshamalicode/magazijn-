package com.prularia.magazijn.Leverancier;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LeverancierControllerTest {
    private static final String LEVERANCIERS_TABLE = "leveranciers";
    private final MockMvc mockMvc;
    private final JdbcClient jdbcClient;

    public LeverancierControllerTest(MockMvc mockMvc, JdbcClient jdbcClient) {
        this.mockMvc = mockMvc;
        this.jdbcClient = jdbcClient;
    }


    @Test
    void findAllReturnsAllLeveranciers() throws Exception {
        mockMvc.perform(get("/leveranciers")).andExpectAll(
                status().isOk(),
                jsonPath("length()").value(JdbcTestUtils.countRowsInTable(jdbcClient, LEVERANCIERS_TABLE)));
    }
}