package com.prularia.magazijn.inkomendeLeveringsLijn;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class InkomendeLeveringsLijnService {
    private final InkomendeLeveringsLijnRepository inkomendeLeveringsLijnRepository;

    public InkomendeLeveringsLijnService(InkomendeLeveringsLijnRepository inkomendeLeveringsLijnRepository) {
        this.inkomendeLeveringsLijnRepository = inkomendeLeveringsLijnRepository;
    }

    @Transactional
    public int createInkomendeLeveringsLijn(InkomendeLeveringsLijn inkomendeLeverings) {
        return inkomendeLeveringsLijnRepository.createInkomendeLeveringsLijn(inkomendeLeverings);
    }

    public List<InkomendeLeveringsLijnDTO>  getLeveringslijnenSortedByMagazijnplaatsId(long inkomendeLeveringsId) {
        return inkomendeLeveringsLijnRepository.getLeveringslijnenSortedByMagazijnplaatsId(inkomendeLeveringsId);
    }
}
