package com.prularia.magazijn.inkomendeLevering;

import com.google.common.collect.Lists;
import com.prularia.magazijn.LeveringsService;
import com.prularia.magazijn.inkomendeLeveringsLijn.InkomendeLeveringsLijn;
import com.prularia.magazijn.inkomendeLeveringsLijn.InkomendeOnvolledigeLeveringslijnDTO;
import com.prularia.magazijn.artikel.ArtikelRepository;
import com.prularia.magazijn.magazijnplaats.Magazijnplaats;
import com.prularia.magazijn.magazijnplaats.MagazijnplaatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class InkomendeLeveringService {

    private final InkomendeLeveringRepository inkomendeLeveringRepository;
    private final LeveringsService leveringsService;

    public InkomendeLeveringService(InkomendeLeveringRepository inkomendeLeveringRepository, MagazijnplaatsRepository magazijnplaatsRepository, ArtikelRepository artikelRepository, LeveringsService leveringsService) {
        this.inkomendeLeveringRepository = inkomendeLeveringRepository;
        this.leveringsService = leveringsService;

    }

    @Transactional
    public long createInkomendeLevering(InkomendeLevering inkomendeLevering) {
        return inkomendeLeveringRepository.createInkomendeLevering(inkomendeLevering);
    }

    @Transactional
    public List<InkomendeLeveringsLijn> vindBestePad(List<InkomendeOnvolledigeLeveringslijnDTO> leveringsLijnen) {
        return leveringsService.<InkomendeOnvolledigeLeveringslijnDTO, InkomendeLeveringsLijn>vindBestePad(
                leveringsLijnen,
                (dto, aantal, plaats) -> new InkomendeLeveringsLijn(
                        dto.getLeveringId(),
                        dto.getArtikelId(),
                        aantal,
                        0, // aantalTeruggestuurd
                        plaats.getMagazijnPlaatsId()
                )
        );
    }

}
