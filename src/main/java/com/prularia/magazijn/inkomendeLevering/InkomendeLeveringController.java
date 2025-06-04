package com.prularia.magazijn.inkomendeLevering;

import com.prularia.magazijn.inkomendeLeveringsLijn.InkomendeLeveringsLijn;
import com.prularia.magazijn.inkomendeLeveringsLijn.InkomendeLeveringsLijnService;
import com.prularia.magazijn.inkomendeLeveringsLijn.InkomendeOnvolledigeLeveringslijnDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("leveringen") //inkomendeleveringen
public class InkomendeLeveringController {
    private final static long ontvangerId = 4L;  //magazijniers Id
    private final InkomendeLeveringService inkomendeLeveringService;
    private final InkomendeLeveringsLijnService inkomendeLeveringsLijnService;

    public InkomendeLeveringController(InkomendeLeveringService inkomendeLeveringService, InkomendeLeveringsLijnService inkomendeLeveringsLijnService) {
        this.inkomendeLeveringService = inkomendeLeveringService;
        this.inkomendeLeveringsLijnService = inkomendeLeveringsLijnService;
    }

    @PostMapping("/{leveranciersId}")
    public long inkomendeLeveringVerwerken(@PathVariable long leveranciersId, @RequestBody InkomendeLeveringDTO inkomendeLeveringDTO) {
        long inkomendeLeveringsId = inkomendeLeveringService.createInkomendeLevering(new InkomendeLevering(0L, leveranciersId, inkomendeLeveringDTO.leveringsbonNummer(), inkomendeLeveringDTO.leveringsbondatum(), inkomendeLeveringDTO.leverDatum(), ontvangerId));
        List<InkomendeOnvolledigeLeveringslijnDTO> inkomendeOnvolledigeLeveringslijnDTOListMetJuisteInkomendeLeveringsId = new ArrayList<>();

        for (InkomendeOnvolledigeLeveringslijnDTO inkomendeOnvolledigeLeveringslijnDTO : inkomendeLeveringDTO.inkomendeOnvolledigeLeveringslijnDTOList()) {
            InkomendeOnvolledigeLeveringslijnDTO updatedRecord = new InkomendeOnvolledigeLeveringslijnDTO(inkomendeLeveringsId, inkomendeOnvolledigeLeveringslijnDTO.artikelId(), inkomendeOnvolledigeLeveringslijnDTO.aantalGoedgekeurd(), inkomendeOnvolledigeLeveringslijnDTO.aantalAfgekeurd());
            inkomendeOnvolledigeLeveringslijnDTOListMetJuisteInkomendeLeveringsId.add(updatedRecord);
        }

        assert !inkomendeOnvolledigeLeveringslijnDTOListMetJuisteInkomendeLeveringsId.isEmpty();
        List<InkomendeLeveringsLijn> inkomendeLeveringsLijnList = inkomendeLeveringService.vindBestePad(inkomendeOnvolledigeLeveringslijnDTOListMetJuisteInkomendeLeveringsId);
        for (var inkomendeLeveringsLijn : inkomendeLeveringsLijnList) {
            inkomendeLeveringsLijnService.createInkomendeLeveringsLijn(inkomendeLeveringsLijn);
        }
        return inkomendeLeveringsId;
    }


}
