package com.prularia.magazijn.uitgaandeLevering;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UitgaandeLeveringsRepositoryTest {
    private final MockMvc mockMvc;
    private final JdbcClient jdbcClient;
    private final UitgaandeLeveringsRepository uitgaandeLeveringsRepository;

    public UitgaandeLeveringsRepositoryTest(MockMvc mockMvc, JdbcClient jdbcClient, UitgaandeLeveringsRepository uitgaandeLeveringsRepository) {
        this.mockMvc = mockMvc;
        this.jdbcClient = jdbcClient;
        this.uitgaandeLeveringsRepository = uitgaandeLeveringsRepository;
    }

    @Test
    void testFindUitgaandeLeveringsStatusId_WhenStatusExists() {
        var statusId = uitgaandeLeveringsRepository.findUitgaandeLeveringsStatusId("RetourInStock");
        assertTrue(statusId.isPresent());
    }

    @Test
    void testFindUitgaandeLeveringsStatusId_WhenStatusDoesNotExist() {
        var statusId = uitgaandeLeveringsRepository.findUitgaandeLeveringsStatusId("NonExistentStatus");
        assertTrue(statusId.isEmpty());
    }

    @Test
    void testUpdateUitgaandeLeveringToRetourInStock() throws Exception {
        long uitgaandeLeveringsId = 1;
        uitgaandeLeveringsRepository.updateUitgaandeLeveringToRetourInStock(uitgaandeLeveringsId);

        var statusId = jdbcClient.sql("SELECT uitgaandeLeveringsStatusId FROM uitgaandeleveringen WHERE uitgaandeLeveringsId = ?")
                .param(uitgaandeLeveringsId).query(Long.class).single();

        assertEquals(6L, statusId);
    }

}
