package com.prularia.magazijn.inkomendeLeveringsLijn;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("inkomendeleveringslijnen")
@RestController
public class InkomendeLeveringsLijnController {
    private final InkomendeLeveringsLijnService inkomendeLeveringsLijnService;

    public InkomendeLeveringsLijnController(InkomendeLeveringsLijnService inkomendeLeveringsLijnService) {
        this.inkomendeLeveringsLijnService = inkomendeLeveringsLijnService;
    }
    @GetMapping("/{inkomendeLeveringsId}")
    public List<InkomendeLeveringsLijnDTO> getInkomendeLeveringslijn(@PathVariable long inkomendeLeveringsId) {
        return inkomendeLeveringsLijnService.getLeveringslijnenSortedByMagazijnplaatsId(inkomendeLeveringsId);
    }
}
