package com.prularia.magazijn;

import com.google.common.collect.Lists;
import com.prularia.magazijn.artikel.ArtikelRepository;
import com.prularia.magazijn.magazijnplaats.Magazijnplaats;
import com.prularia.magazijn.magazijnplaats.MagazijnplaatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class LeveringsService {
    private final ArtikelRepository artikelRepository;
    private final MagazijnplaatsRepository magazijnplaatsRepository;

    public LeveringsService(ArtikelRepository artikelRepository,
                            MagazijnplaatsRepository magazijnplaatsRepository) {
        this.artikelRepository = artikelRepository;
        this.magazijnplaatsRepository = magazijnplaatsRepository;
    }

    /**
     * 🔹 Generieke methode voor het vinden van het beste pad voor leveringen en retouren.
     * Werkt voor zowel inkomende leveringen als bestellingen.
     */
    @Transactional
    public <T extends LeveringsLijnDTO, R> List<R> vindBestePad(
            List<T> leveringsLijnen, BestelLijnCreator<T, R> creator) {

        var alleCombinaties = new ArrayList<List<List<Magazijnplaats>>>();
        var hoeveelhedenNodig = new HashMap<Long, Long>();

        for (var lijn : leveringsLijnen) {
            long artikelId = lijn.getArtikelId();
            long nodig = lijn.getAantal();

            var plaatsen = magazijnplaatsRepository.findPlaatsenByArtikelId(artikelId);
            hoeveelhedenNodig.put(artikelId, nodig);

            while (aantalBeschikbaar(plaatsen, artikelId) < nodig) {
                magazijnplaatsRepository.setArtikel(artikelId);
                plaatsen = magazijnplaatsRepository.findPlaatsenByArtikelId(artikelId);
            }

            var mogelijkeCombinaties = combinatiesPerArtikel(new LinkedList<>(plaatsen));
            var geldigeCombinaties = mogelijkeCombinaties.stream()
                    .filter(combo -> !combo.isEmpty() && aantalBeschikbaar(combo, artikelId) >= nodig)
                    .toList();

            alleCombinaties.add(geldigeCombinaties);
        }

        var allePaden = Lists.cartesianProduct(alleCombinaties);

        List<Magazijnplaats> bestePad = new ArrayList<>();
        int besteScore = Integer.MAX_VALUE;
        for (var pad : allePaden) {
            var gesorteerdPad = maakGesorteerdPad(pad);
            var score = berekenScore(gesorteerdPad);
            if (score < besteScore) {
                besteScore = score;
                bestePad = gesorteerdPad;
            }
        }

        var resultaat = new ArrayList<R>();
        for (var plaats : bestePad) {
            var matchingDto = leveringsLijnen.stream()
                    .filter(dto -> dto.getArtikelId() == plaats.getArtikelId())
                    .findFirst()
                    .orElse(null);

            if (matchingDto == null) continue;

            var aantalVoorDezeLijn = Math.min(hoeveelhedenNodig.get(plaats.getArtikelId()),
                    artikelRepository.vindMaxAantalInMagazijnPLaats(plaats.getArtikelId()) - plaats.getAantal());
            hoeveelhedenNodig.put(plaats.getArtikelId(), hoeveelhedenNodig.get(plaats.getArtikelId()) - aantalVoorDezeLijn);

            // 🔹 Gebruik de meegegeven creator om een object van type R te maken
            resultaat.add(creator.create(matchingDto, aantalVoorDezeLijn, plaats));
        }
        return resultaat;
    }

    /**
     * 🔹 Retourneert de totale vrije ruimte voor een artikel in een lijst met magazijnplaatsen.
     */
    private int aantalBeschikbaar(List<Magazijnplaats> plaatsen, long artikelId) {
        var maxAantal = artikelRepository.vindMaxAantalInMagazijnPLaats(artikelId);
        return plaatsen.stream()
                .mapToInt(p -> (int) (maxAantal - p.getAantal()))
                .sum();
    }

    /**
     * 🔹 Genereert alle mogelijke combinaties van magazijnplaatsen voor een artikel.
     */
    private List<List<Magazijnplaats>> combinatiesPerArtikel(LinkedList<Magazijnplaats> plaatsen) {
        var result = new LinkedList<List<Magazijnplaats>>();
        if (plaatsen.size() == 1) {
            result.add(new LinkedList<>());
            result.add(List.of(plaatsen.get(0)));
            return result;
        }

        var first = plaatsen.removeFirst();
        var subCombinaties = combinatiesPerArtikel(plaatsen);
        for (var combo : subCombinaties) {
            result.add(combo);
            var extra = new LinkedList<>(combo);
            extra.add(first);
            result.add(extra);
        }
        plaatsen.addFirst(first);
        return result;
    }

    /**
     * 🔹 Sorteert het pad (lijst van lijst van magazijnplaatsen) op rij+rek.
     */
    private List<Magazijnplaats> maakGesorteerdPad(List<List<Magazijnplaats>> pad) {
        var resultaat = new ArrayList<Magazijnplaats>();
        for (var deel : pad) {
            resultaat.addAll(deel);
        }
        // Sorteer alfabetisch op rij en rek
        resultaat.sort(Comparator
                .comparing(Magazijnplaats::getRij)
                .thenComparing(Magazijnplaats::getRek));
        return resultaat;
    }

    /**
     * 🔹 Bereken een score voor een route door het magazijn.
     */
    private int berekenScore(List<Magazijnplaats> plaatsen) {
        Map<String, Long> maxRekPerRij = new HashMap<>();
        for (var p : plaatsen) {
            maxRekPerRij.put(
                    p.getRij(),
                    Math.max(maxRekPerRij.getOrDefault(p.getRij(), 0L), p.getRek())
            );
        }
        return maxRekPerRij.values().stream().mapToInt(Long::intValue).sum();
    }
}
