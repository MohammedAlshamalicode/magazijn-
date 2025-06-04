package com.prularia.magazijn.inkomendeLeveringsLijn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InkomendeLeveringslijnControllerTest {
    private final MockMvc mockMvcTester;


    public InkomendeLeveringslijnControllerTest(MockMvc mockMvcTester) {
        this.mockMvcTester = mockMvcTester;

    }

    @Test
    void getSortedLeveringslijnenWithExistingDataReturnsResults() throws Exception {

        mockMvcTester.perform(get("/inkomendeleveringslijnen"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].inkomendeLeveringsId").isNumber(),
                        jsonPath("$[0].artikelId").isNumber(),
                        jsonPath("$[0].naam").isString(),
                        jsonPath("$[0].beschrijving").isString(),
                        jsonPath("$[0].aantalGoedgekeurd").isNumber(),
                        jsonPath("$[0].aantalTeruggestuurd").isNumber(),
                        jsonPath("$[0].magazijnPlaatsId").isNumber(),
                        jsonPath("$[0].plaats").isString()
                );
    }
}
