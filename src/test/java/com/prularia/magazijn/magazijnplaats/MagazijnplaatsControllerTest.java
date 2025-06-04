package com.prularia.magazijn.magazijnplaats;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MagazijnplaatsControllerTest {

    private final MockMvc mockMvc;
    private final Path testResources = Path.of("src/test/resources");

    public MagazijnplaatsControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void verwerkLeveringWerkt() throws Exception {

        var json = Files.readString(testResources.resolve("verwerkleveringen.json"));

        var response = mockMvc.perform(post("/magazijnplaats/aanvullenInRek")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo("Levering succesvol verwerkt!");


    }
}
