package com.prularia.magazijn.pickingLocatie;

import com.prularia.magazijn.magazijnplaats.MagazijnplaatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional(readOnly = true)
public class PickingLocatieService {

    private final MagazijnplaatsRepository magazijnPlaatsRepository;

    public PickingLocatieService(MagazijnplaatsRepository magazijnPlaatsRepository) {
        this.magazijnPlaatsRepository = magazijnPlaatsRepository;
    }

    public List<PickingLocatie> getOptimizedPickingPath(long bestelId) {

        Map<String, List<PickingLocatie>> groupedByCell = magazijnPlaatsRepository.findGroupedByCellAndOrdered(bestelId);


        // Group locaties by Artikel ID
        List<PickingLocatie> optimizedPath = new ArrayList<>();
        PickingLocatie currentLocation = null;

        List<Map.Entry<String, List<PickingLocatie>>> sortedGroups = new ArrayList<>(groupedByCell.entrySet());

        // Sort groups by proximity to the current location
        PickingLocatie finalCurrentLocation = currentLocation;
        sortedGroups.sort(Comparator.comparing(entry -> calculateScore(finalCurrentLocation, entry.getValue().get(0))));

        for (var entry : sortedGroups) {
            List<PickingLocatie> cellLocaties = new ArrayList<>(entry.getValue());

            for (PickingLocatie locatie : cellLocaties) {
                long requiredQuantity = locatie.getAantalBesteld();
                long pickedQuantity = optimizedPath.stream()
                        .filter(p -> p.getArtikelId() == locatie.getArtikelId())
                        .mapToLong(PickingLocatie::getAantalBesteld)
                        .sum();

                if (pickedQuantity < requiredQuantity) {
                    long remainingQuantity = requiredQuantity - pickedQuantity;
                    long pickable = Math.min(locatie.getVoorraadInPlaats(), remainingQuantity);
                    optimizedPath.add(createUpdatedPickingLocatie(locatie, pickable));
                }
            }

            currentLocation = cellLocaties.getLast();
        }
        return optimizedPath;
    }

    private PickingLocatie createUpdatedPickingLocatie(PickingLocatie locatie, long pickable) {
        return new PickingLocatie(
                locatie.getArtikelId(),
                locatie.getArtikelNaam(),
                locatie.getMagazijnPlaatsId(),
                locatie.getRij(),
                locatie.getRek(),
                locatie.getVoorraadInPlaats(),
                pickable,
                locatie.getBeschrijving()
        );
    }

    private int calculateScore(PickingLocatie current, PickingLocatie next) {
        if (current == null) {
            return getRijAsInt(next.getRij()) * 1000 + next.getRek();
        }
        int rijDistance = Math.abs(getRijAsInt(current.getRij()) - getRijAsInt(next.getRij()));
        int rekDistance = Math.abs(current.getRek() - next.getRek());
        return rijDistance * 1000 + rekDistance;
    }

    private int getRijAsInt(char rij) {
        return rij - 'A' + 1;
    }
}


