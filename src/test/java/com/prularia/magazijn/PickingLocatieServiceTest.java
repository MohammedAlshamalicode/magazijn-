package com.prularia.magazijn;

import com.prularia.magazijn.magazijnplaats.MagazijnplaatsRepository;
import com.prularia.magazijn.pickingLocatie.PickingLocatie;
import com.prularia.magazijn.pickingLocatie.PickingLocatieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
class PickingLocatieServiceTest {
    private PickingLocatieService pickingLocatieService;
    private MagazijnplaatsRepository magazijnPlaatsRepository;

    PickingLocatieServiceTest(PickingLocatieService pickingLocatieService, MagazijnplaatsRepository magazijnPlaatsRepository) {
        this.pickingLocatieService = pickingLocatieService;
        this.magazijnPlaatsRepository = magazijnPlaatsRepository;
    }


    @BeforeEach
    void setUp() {
        magazijnPlaatsRepository = mock(MagazijnplaatsRepository.class);
        pickingLocatieService = new PickingLocatieService(magazijnPlaatsRepository);
    }


    @Test
    void  Get_Optimized_Picking_Path_Returns_Correct_Order_And_Items() {

        long bestelId = 1L;
        Map<String, List<PickingLocatie>> groupedByCell = Map.of(
                "A-1", List.of(
                        new PickingLocatie(1L, "Artikel A", 101L, 'A', 1, 10, 5, "")
                ),
                "A-2", List.of(
                        new PickingLocatie(2L, "Artikel B", 102L, 'A', 2, 15, 10, "")
                )
        );

        when(magazijnPlaatsRepository.findGroupedByCellAndOrdered(bestelId)).thenReturn(groupedByCell);


        List<PickingLocatie> result = pickingLocatieService.getOptimizedPickingPath(bestelId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getArtikelId());
        assertEquals(2L, result.get(1).getArtikelId());
    }

    @Test
    void Get_Optimized_Picking_Path_WithEmpty_Result() {

        long bestelId = 2L;
        when(magazijnPlaatsRepository.findGroupedByCellAndOrdered(bestelId)).thenReturn(Map.of());

        List<PickingLocatie> result = pickingLocatieService.getOptimizedPickingPath(bestelId);


        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void Get_Optimized_PickingPath_Prioritize_Cell_For_Multiple_Articles() {

        long bestelId = 1L;
        Map<String, List<PickingLocatie>> groupedByCell = Map.of(
                "A-5", List.of(
                        new PickingLocatie(1L, "Artikel1", 5L, 'A', 5, 20, 5, "")
                ),
                "A-10", List.of(
                        new PickingLocatie(2L, "Artikel2", 10L, 'A', 10, 50, 10, "")
                ),
                "A-40", List.of(
                        new PickingLocatie(3L, "Artikel3", 40L, 'A', 40, 30, 5, "")
                ),
                "B-1", List.of(
                        new PickingLocatie(4L, "Artikel1", 1L, 'B', 1, 20, 5, "")//same item(A-5)
                )
        );

        when(magazijnPlaatsRepository.findGroupedByCellAndOrdered(bestelId)).thenReturn(groupedByCell);


        List<PickingLocatie> result = pickingLocatieService.getOptimizedPickingPath(bestelId);


        assertThat(result)
                .isNotNull()
                .hasSize(4)
                .extracting(PickingLocatie::getArtikelId)
                .containsExactly(1L, 2L, 3L,4L);

        assertThat(result.get(0).getRij()).isEqualTo('A');//Artikel1(1st)
        assertThat(result.get(0).getRek()).isEqualTo(5);//idem
        assertThat(result.get(1).getRij()).isEqualTo('A');//ArtiKel2(2d)
        assertThat(result.get(1).getRek()).isEqualTo(10);//idem
        assertThat(result.get(2).getRij()).isEqualTo('A');//ArtiKel3(3d)
        assertThat(result.get(2).getRek()).isEqualTo(40);//idem
        assertThat(result.get(3).getRij()).isEqualTo('B');//ArtiKel1(4th)
        assertThat(result.get(3).getRek()).isEqualTo(1);//idem
    }

    @Test
    void Get_OptimizedPickingPath_WithIncompleteItems() {// Only one location should be picked

        long bestelId = 1L;
        Map<String, List<PickingLocatie>> groupedByCell = Map.of(
                "A-10", List.of(
                        new PickingLocatie(1L, "Artikel1", 10L, 'A', 10, 10, 3, ""), // 3 items
                        new PickingLocatie(1L, "Artikel1", 11L, 'A', 10, 10, 2, "")  // 2 items
                )
        );

        when(magazijnPlaatsRepository.findGroupedByCellAndOrdered(bestelId)).thenReturn(groupedByCell);


        List<PickingLocatie> result = pickingLocatieService.getOptimizedPickingPath(bestelId);

        assertThat(result)
                .isNotNull()
                .hasSize(1) // Only one location should be picked
                .extracting(PickingLocatie::getAantalBesteld)
                .containsExactly(3L); // Total 3 items
    }




}
