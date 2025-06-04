package com.prularia.magazijn.bestelling;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BestellijnService {
    private final BestellijnRepository bestellijnRepository;

    public BestellijnService(BestellijnRepository bestellijnRepository) {
        this.bestellijnRepository = bestellijnRepository;
    }

    public List<BestellijnDTO> findByBestelId(long bestelId) {
        return bestellijnRepository.findByBestelId(bestelId);
    }
}
