"use strict";
import {verberg, byId, toon} from "./util.js";

document.addEventListener("DOMContentLoaded", async function () {
    verbergAlleFouten();
    await leveranciersTonen(); //gaat namen van leveranciers ophalen uit database en vult daarmee drop-down
    //hieronder 2 functies om ingevulde gegevens uit Local Storage te halen mocht de tablet uitgevallen zijn
    algemeneGegevensOphalen();
    haalLijnenUitLocalStorage();
})

//voor elk select of input veld een trigger die aangepaste gegevens opslaat in Local Storage
byId("leverancier").addEventListener("change", () => {
    localStorage.setItem("leveranciersId", JSON.stringify(Number(byId("leverancier").value))) //leveranciersId apart opslaan om te gebruiken in path voor POST
    verberg("leverancierFout")
});

byId("leveringsbonnummer").addEventListener("change", () => {
    algemeneGegevens()

    if (byId("leveringsbonnummer").value.trim === null || byId("leveringsbonnummer").value.trim === "") {
        toon("leveringsbonnummerFout")
    } else {
        verberg("leveringsbonnummerFout")
    }
});
byId("leveringsbondatum").addEventListener("change", () => {
    algemeneGegevens()
    verberg("leveringsbondatumFout")
});
byId("leverdatum").addEventListener("change", ()=> {
    algemeneGegevens()
    verberg("leverdatumFout")
});

byId("ean").addEventListener("keypress", (e) => {
    if (e.key === 'Enter') {
        e.preventDefault();
        verberg("eanFout");
        verberg("eanBestaatFout");
        valideerEANenZoekArtikel(); //valideer of iets werd ingegeven, of artikel met deze EAN reeds toegevoegd werd, zoniet artikel opzoeken in database
    }
})

//bij klikken op button "bevestig"
//controleren of verplichte velden ingevuld/geselecteerd werden, behalve die om EAN in te geven
//leveranciersId gebruiken in path voor POST, InkomendeLevering uit Local Storage wordt doorgestuurd met volgende info
//  {   leveringsbonNummer: ,
//      leveringsbondatum: ,
//      leverDatum: ,
//      inkomendeOnvolledigeLeveringslijnDTOList: }
//wanneer POST lukt bevat de response inkomendeLeveringId, dit wordt opgeslagen in Local Storage zodat leveringslijnen.html de lijnen op basis hiervan kan terugvinden in juiste volgorde
byId("bevestig").onclick = async () => {
    const fouten = document.querySelectorAll("input:invalid, select:invalid");
    if (fouten.length > 1) {
        for (const fout of fouten) {
            if (fout.id !== "ean") {
                toon(`${fout.id}Fout`);
            }
        }
    }
    if (byId("leveringsBody").children.length === 0) {
        toon("eanFout")
    } else {
        verberg("eanFout")
        if (fouten.length === 1 && fouten[0].id === "ean") {
            const leveranciersId = JSON.parse(localStorage.getItem("leveranciersId"));
            const response = await fetch(`leveringen/${leveranciersId}`,
                {
                    method: "POST",
                    headers: {'Content-Type': "application/json"},
                    body: localStorage.getItem("InkomendeLevering")
                });
            if (response.ok) {
                const inkomendeLeveringId = await response.json();
                localStorage.clear()
                localStorage.setItem("leveringsId", inkomendeLeveringId);
                verbergAlleFouten()
                byId("leveringsBody").innerHTML = "";
                window.location = "leveringslijnen.html"
            } else {
                toon("storing")
            }
        }
    }
}

function verbergAlleFouten() {
    verberg("storing");
    verberg("leverancierFout");
    verberg("leveringsbonnummerFout");
    verberg("leveringsbondatumFout");
    verberg("leverdatumFout");
    verberg("eanFout");
    verberg("eanBestaatFout")
}

//namen van leveranciers ophalen uit database en daarmee drop-down vullen, value die wordt opgeslagen is leveranciersId want deze is nodig in path voor POST
async function leveranciersTonen() {
    const response = await fetch(`leveranciers`);
    if (response.ok) {
        const leveranciers = await response.json();
        const leverancierDropDown = byId("leverancier");
        for (const leverancier of leveranciers) {
            const option = document.createElement("option");
            option.value = leverancier.leveranciersId;
            option.textContent = leverancier.naam;
            leverancierDropDown.appendChild(option);
        }
    } else {
        toon("storing");
    }
}

//alle values nodig voor de POST opslaan in Local Storage
function algemeneGegevens() {
    const leverbon = {
        leveringsbonNummer: byId("leveringsbonnummer").value,
        leveringsbondatum: byId("leveringsbondatum").value,
        leverDatum: byId("leverdatum").value,
        inkomendeOnvolledigeLeveringslijnDTOList: JSON.parse(localStorage.getItem("inkomendeLeveringsLijnen")),
    }
    localStorage.setItem("InkomendeLevering", JSON.stringify(leverbon));
}

//ingevulde gegevens uit Local Storage te halen mocht de tablet uitgevallen zijn
function algemeneGegevensOphalen() {
    if (localStorage.getItem("leveranciersId") !== null) {
        const leveranciersId = JSON.parse(localStorage.getItem("leveranciersId"));
        byId("leverancier").value = Number(leveranciersId);
    }
    if (localStorage.getItem("InkomendeLevering") !== null) {
        const leverbon = JSON.parse(localStorage.getItem("InkomendeLevering"));
        byId("leveringsbonnummer").value = leverbon.leveringsbonNummer;
        byId("leveringsbondatum").value = leverbon.leveringsbondatum;
        byId("leverdatum").value = leverbon.leverDatum;
    }
}

//ingevulde gegevens uit Local Storage te halen mocht de tablet uitgevallen zijn
function haalLijnenUitLocalStorage() {
    const data = JSON.parse(localStorage.getItem("data"));
    if (data !== null) {
        data.forEach(lijn => {
            zoekArtikel(lijn.ean)
        })
    }
}

//valideer EAN:
//  werd niets ingevuld => toon eanFout ("verplicht")
//  werd wel iets ingevuld => kijk of EAN reeds voorkomt in Local Storage
//  komt niet voor => roep functie op om artikel in database te zoeken
//  komt wel voor => toon eanBestaatFout
function valideerEANenZoekArtikel() {
    const eanInput = byId("ean");
    if (!eanInput.checkValidity()) {
        verberg("eanBestaatFout")
        toon("eanFout");
        eanInput.focus();
    } else {
        verberg("eanFout");
        const data = JSON.parse(localStorage.getItem("data"))
        let check = false
        if (data !== null) {
            data.forEach(lijn => {
                if (lijn.ean === eanInput.value)
                    check = true;
            })
        }
        if (check === false) {
            verberg("eanBestaatFout")
            zoekArtikel(eanInput.value);
        } else {
            verberg("nietGevonden")
            toon("eanBestaatFout")
        }
    }
}

//zoek artikel in database op basis van EAN
//  artikel gevonden => roep functie op nieuwe lijn in tabel aan te maken met gegevens van artikel
//  niet gevonden => toon foutmelding
async function zoekArtikel(ean) {
    const response = await fetch(`artikelen?ean=${ean}`);
    if (response.ok) {
        verberg("nietGevonden")
        byId("ean").value = "";
        const artikel = await response.json();
        verwerkInkomendeLeveringsLijnen(artikel)
        return
    }
    if (response.status === 404) {
        toon("nietGevonden");
    } else {
        toon("storing");
    }
}

//functie om nieuwe lijn in tabel aan te maken met gegevens van artikel
//4 kolommen voor:
//                  EAN
//                  naam artikel (zo kan leverancier beter verifiëren of hij juist ingegeven heeft)
//                  aantal goedgekeurd
//                  aantal teruggestuurd
//functie oproepen om gegevens van deze lijn toe te voegen aan Local Storage
//functie oproepen om deze gegevens ook op te slaan bij alle values nodig voor de POST
//voor elk input veld een trigger die aangepaste gegevens opslaat in Local Storage
function verwerkInkomendeLeveringsLijnen(artikel) {
    const leveringsBody = byId("leveringsBody");
    const tr = leveringsBody.insertRow()

    const ean = tr.insertCell();
    ean.textContent = artikel.ean;

    const naam = tr.insertCell();
    naam.textContent = artikel.naam;

    const aantalGoedgekeurd = tr.insertCell();
    const aantalGoedgekeurdInput = document.createElement("input");
    aantalGoedgekeurdInput.min = '0';
    aantalGoedgekeurdInput.value = '0';
    aantalGoedgekeurdInput.type = "number";
    aantalGoedgekeurd.appendChild(aantalGoedgekeurdInput);

    const aantalTerugGestuurd = tr.insertCell();
    const aantalTerugGestuurdInput = document.createElement("input");
    aantalTerugGestuurdInput.min = '0';
    aantalTerugGestuurdInput.value = '0';
    aantalTerugGestuurdInput.type = "number";
    aantalTerugGestuurd.appendChild(aantalTerugGestuurdInput);

    artikelLijnInLocalStorage(artikel, aantalTerugGestuurdInput.value, aantalGoedgekeurdInput.value);
    algemeneGegevens()

    aantalTerugGestuurdInput.addEventListener("change", () => {
        artikelLijnInLocalStorage(artikel, aantalTerugGestuurdInput.value, aantalGoedgekeurdInput.value);
        algemeneGegevens()
    });
    aantalGoedgekeurdInput.addEventListener("change", () => {
        artikelLijnInLocalStorage(artikel, aantalTerugGestuurdInput.value, aantalGoedgekeurdInput.value);
        algemeneGegevens()
    })
}

//2 keys zijn nodig om te bewaren in Local Storage
//  1 met gegevens die moeten doorgegeven worden met de POST
//  1 met gegevens die nodig zijn om de lijnen opnieuw te laden mocht de tablet zijn uitgevallen
//de gegevens worden voor allebei in een "voorlopige" variabele bijgehouden
//er wordt voor beide gekeken of de key reeds bestaat in Local Storage, zoniet wordt een lege array gemaakt
//komt het artikel reeds voor in de gegevens die moeten doorgegeven worden met de POST
//  (dan komt het ook voor in de andere gegevens én op dezelfde index)
//  overschrijf dan de beide gegevens met de gegevens uit de voorlopige variabele
//zoniet voeg het dan toe
//daarna de gegevens voor allebei nog opslagen in Local Storage
function artikelLijnInLocalStorage(artikel, aantalTerugGestuurdInput, aantalGoedgekeurdInput) {
    let voorlopigeArray = {
        leveringId: 0,
        artikelId: artikel.artikelId,
        aantalGoedgekeurd: `${aantalGoedgekeurdInput}`,
        aantalAfgekeurd: `${aantalTerugGestuurdInput}`,
    }
    let inkomendeLeveringsLijnen = JSON.parse(localStorage.getItem("inkomendeLeveringsLijnen")) || [];

    let VoorlopigeDataArray = {
        ean: artikel.ean,
        naam: artikel.naam,
        aantalGoedgekeurd: `${aantalGoedgekeurdInput}`,
        aantalAfgekeurd: `${aantalTerugGestuurdInput}`,
    }
    let data = JSON.parse(localStorage.getItem("data")) || [];

    const bestaandeLijnIndex = inkomendeLeveringsLijnen.findIndex(line => line.artikelId === artikel.artikelId);

    if (bestaandeLijnIndex > -1) {
        inkomendeLeveringsLijnen[bestaandeLijnIndex] = voorlopigeArray
        data[bestaandeLijnIndex] = VoorlopigeDataArray
    } else {
        inkomendeLeveringsLijnen.push(voorlopigeArray);
        data.push(VoorlopigeDataArray)
    }
    localStorage.setItem("inkomendeLeveringsLijnen", JSON.stringify(inkomendeLeveringsLijnen));
    localStorage.setItem("data", JSON.stringify(data));
}