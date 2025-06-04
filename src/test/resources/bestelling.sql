insert into bestellingen (besteldatum, klantId, betaald, betaalwijzeId, annulatie, bestellingsStatusId,
                          actiecodeGebruikt, voornaam, familienaam, facturatieAdresId, leveringsAdresId)
values (now(), 1, 1, 1, 0, 1, 0,
        'Test Voornaam', 'Test Familienaam', 1, 1);

insert into bestellijnen (bestelId, artikelId, aantalBesteld, aantalGeannuleerd)
values ((select bestelId from bestellingen where voornaam = 'Test Voornaam'), 1, 2, 0),
       ((select bestelId from bestellingen where voornaam = 'Test Voornaam'), 2, 4, 0),
       ((select bestelId from bestellingen where voornaam = 'Test Voornaam'), 3, 6, 1)