package com.prularia.magazijn.bestelling;

import com.prularia.magazijn.pickingLocatie.PickingLocatie;
import com.prularia.magazijn.pickingLocatie.PickingLocatieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("bestelling")
public class BestellijnController {
    private final PickingLocatieService pickingLocatieService;

    public BestellijnController(PickingLocatieService pickingLocatieService) {
        this.pickingLocatieService = pickingLocatieService;
    }

    @GetMapping("{id}")
    public Stream<BestellijnDTO> getBestellijnen(@PathVariable long id) {
        var pickingPath = pickingLocatieService.getOptimizedPickingPath(id);
        return pickingPath.stream().map(this::bestellijnDTOVanPickingLocatie);
    }

    private BestellijnDTO bestellijnDTOVanPickingLocatie(PickingLocatie locatie) {
        return new BestellijnDTO(locatie.getArtikelId(), locatie.getArtikelNaam(), locatie.getBeschrijving(),
                locatie.getAantalBesteld(), "" + locatie.getRij() + locatie.getRek());
    }

}