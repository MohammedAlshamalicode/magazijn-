insert into bestellingen (besteldatum, klantId, betaald, betaalwijzeId, annulatie, bestellingsStatusId,
                          actiecodeGebruikt, voornaam, familienaam, facturatieAdresId, leveringsAdresId)
values (now(), 1, 1, 1, 0, 4,
        false, 'Test Voornaam', 'Test Familienaam', 1, 1);

insert into bestellijnen (bestelId, artikelId, aantalBesteld)
values ((select bestelId from bestellingen where voornaam = 'Test Voornaam'), 1, 2),
       ((select bestelId from bestellingen where voornaam = 'Test Voornaam'), 2, 3),
       ((select bestelId from bestellingen where voornaam = 'Test Voornaam'), 3, 4)