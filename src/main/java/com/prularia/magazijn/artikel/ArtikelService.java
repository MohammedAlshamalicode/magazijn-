package com.prularia.magazijn.artikel;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
class ArtikelService {
    private final ArtikelRepository artikelRepository;

    public ArtikelService(ArtikelRepository artikelRepository) {
        this.artikelRepository = artikelRepository;
    }

    Optional<Artikel> findById(long id) {
        return artikelRepository.findById(id);
    }

    ArtikelDTO findByEAN(String ean) {
        return artikelRepository.findArtikelIdByEAN(ean).
            orElseThrow(() -> new ArtikelNietGevondenException(ean));}
}
