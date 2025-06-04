package com.prularia.magazijn.bestelling;

import java.util.List;

public record AfgerondeBestellingDTO(long bestelId, List<AfgerondeBestellijnDTO> bestellijnenDTOs) {
}
