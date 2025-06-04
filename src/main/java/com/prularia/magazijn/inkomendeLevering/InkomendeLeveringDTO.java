package com.prularia.magazijn.inkomendeLevering;

import com.prularia.magazijn.inkomendeLeveringsLijn.InkomendeOnvolledigeLeveringslijnDTO;

import java.time.LocalDate;
import java.util.List;

public record InkomendeLeveringDTO(
        String leveringsbonNummer,
        LocalDate leveringsbondatum,
        LocalDate leverDatum,
        List<InkomendeOnvolledigeLeveringslijnDTO> inkomendeOnvolledigeLeveringslijnDTOList
) {
}
