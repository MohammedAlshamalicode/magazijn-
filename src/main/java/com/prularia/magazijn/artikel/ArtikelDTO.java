package com.prularia.magazijn.artikel;

public record ArtikelDTO(long artikelId,String ean,String naam, String beschrijving, int voorraad) { }