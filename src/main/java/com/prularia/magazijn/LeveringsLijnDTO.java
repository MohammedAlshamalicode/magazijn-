package com.prularia.magazijn;

/**
 * 🔹 Interface voor leveringslijnen (zowel inkomend als uitgaand).
 * Wordt geïmplementeerd door verschillende DTO’s zoals:
 * - InkomendeOnvolledigeLeveringslijnDTO (voor inkomende leveringen)
 * - Bestellijn (voor bestellingen en retouren)
 */
public interface LeveringsLijnDTO {

    /**
     * @return De unieke ID van de levering of bestelling waartoe deze lijn behoort.
     */
    long getLeveringId();

    /**
     * @return Het ID van het artikel dat wordt geleverd of geretourneerd.
     */
    long getArtikelId();

    /**
     * @return Het aantal eenheden van het artikel dat verwerkt moet worden.
     */
    long getAantal();
}

