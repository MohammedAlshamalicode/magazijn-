package com.prularia.magazijn.magazijnplaats;

import com.prularia.magazijn.artikel.ArtikelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MagazijnplaatsService {
    private final MagazijnplaatsRepository magazijnplaatsRepository;
    private final ArtikelRepository artikelRepository;


    public MagazijnplaatsService(MagazijnplaatsRepository magazijnplaatsRepository, ArtikelRepository artikelRepository) {
        this.magazijnplaatsRepository = magazijnplaatsRepository;
        this.artikelRepository = artikelRepository;
    }

    @Transactional
    public void aanvullenInRek(List<AanvullenInRekDTO> dtos) {

        for (AanvullenInRekDTO dto : dtos) {
        // Haal het huidige aantalGoedgekeurd van de magazijnplaats op
        int huidigAantal = magazijnplaatsRepository.getHuidigAantalOpMagazijnplaats(dto.magazijnPlaatsId());

        // Haal het maximale aantalGoedgekeurd in de magazijnplaats op
        int maxAantal = magazijnplaatsRepository.getMaxAantalInMagazijnplaats(dto.artikelId());

        // Controleer of het aantalGoedgekeurd niet boven de limiet uitkomt
        if (huidigAantal + dto.aantalGoedgekeurd() > maxAantal) {
            throw new IllegalArgumentException("De som van het huidig aantalGoedgekeurd en toegevoegd aantalGoedgekeurd overschrijdt de limiet van de magazijnplaats.");

        }

        // Voorraad aanvullen in artikelen
        artikelRepository.voorraadAanvullen(dto.artikelId(), dto.aantalGoedgekeurd());

        // Specifieke magazijnplaats aanvullen
        magazijnplaatsRepository.aanvullenMagazijnplaats(dto.magazijnPlaatsId(), dto.aantalGoedgekeurd());
    }
    }
}
