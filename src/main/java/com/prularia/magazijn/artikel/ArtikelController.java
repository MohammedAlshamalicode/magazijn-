package com.prularia.magazijn.artikel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ArtikelController {
    private final ArtikelService artikelService;

    public ArtikelController(ArtikelService artikelService) {
        this.artikelService = artikelService;
    }

    @GetMapping("artikelen/{id}")
    Artikel findById(@PathVariable long id) {
        return artikelService.findById(id).orElseThrow(() -> new ArtikelNietGevondenException(id));
    }

    @GetMapping(value = "artikelen", params = "ean")
    public ArtikelDTO findByEAN(String ean) {
        return artikelService.findByEAN(ean);
    }
}
