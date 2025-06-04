package com.prularia.magazijn.leverancier;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("leveranciers")
public class LeverancierController {
    private final LeverancierService leverancierService;

    public LeverancierController(LeverancierService leverancierService) {
        this.leverancierService = leverancierService;
    }

    @GetMapping
    public List<LeverancierNaamEnIdDTO> getAllLeveranciers() {
        return leverancierService.findAllLeveranciers();
    }
}
