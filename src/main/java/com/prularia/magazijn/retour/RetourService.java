package com.prularia.magazijn.retour;

import com.prularia.magazijn.artikel.ArtikelRepository;
import com.prularia.magazijn.bestelling.*;
import com.prularia.magazijn.magazijnplaats.Magazijnplaats;
import com.prularia.magazijn.magazijnplaats.MagazijnplaatsDTO;
import com.prularia.magazijn.magazijnplaats.MagazijnplaatsNietGevondenException;
import com.prularia.magazijn.magazijnplaats.MagazijnplaatsRepository;
import com.prularia.magazijn.uitgaandeLevering.UitgaandeLeveringsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RetourService {
    private final BestellingRepository bestellingRepository;
    private final MagazijnplaatsRepository magazijnplaatsRepository;
    private final UitgaandeLeveringsRepository uitgaandeLeveringsRepository;
    private final BestellijnRepository bestellijnRepository;
    private final ArtikelRepository artikelRepository;

    public RetourService(BestellingRepository bestellingRepository, MagazijnplaatsRepository magazijnplaatsRepository, UitgaandeLeveringsRepository uitgaandeLeveringsRepository, BestellijnRepository bestellijnRepository, ArtikelRepository artikelRepository) {
        this.bestellingRepository = bestellingRepository;
        this.magazijnplaatsRepository = magazijnplaatsRepository;
        this.uitgaandeLeveringsRepository = uitgaandeLeveringsRepository;
        this.bestellijnRepository = bestellijnRepository;
        this.artikelRepository = artikelRepository;
    }

    @Transactional
    public void verwerkRetour(long bestelId) {

        List<BestellijnMetArtikel> bestellijnen = bestellijnRepository.findBestellijnByBestelId(bestelId);
        // Producten terugbrengen naar de voorraad met behulp van verhoogMagazijnplaatsVoorraad
        for (var bestellijn : bestellijnen) {
            List<MagazijnplaatsDTO> locations = magazijnplaatsRepository.findBeschikbarePlaatsenVoorArtikel(bestellijn.artikelId());

            magazijnplaatsRepository.verhoogMagazijnplaatsVoorraad(locations, bestellijn.aantal());
            artikelRepository.verhoogVoorraad(bestellijn.artikelId(), bestellijn.aantal());
        }
        // Update de verzendstatus naar "RetourInStock"
        uitgaandeLeveringsRepository.updateUitgaandeLeveringToRetourInStock(bestelId);
    }


}
