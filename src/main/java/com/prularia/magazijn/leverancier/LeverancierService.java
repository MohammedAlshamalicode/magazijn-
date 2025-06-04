package com.prularia.magazijn.leverancier;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeverancierService {
    private final LeverancierRepository leverancierRepository;

    public LeverancierService(LeverancierRepository leverancierRepository) {
        this.leverancierRepository = leverancierRepository;
    }

    public List<LeverancierNaamEnIdDTO> findAllLeveranciers() {
        return leverancierRepository.findAllLeveranciers();
    }
}
