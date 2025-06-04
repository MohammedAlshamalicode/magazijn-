"use strict"
import {setText, toon, verberg} from "./util.js";

document.addEventListener("DOMContentLoaded", async () => {
    verberg("info", "nietGevonden", "storing");
    const artikelId = JSON.parse(sessionStorage.getItem("artikelId"))
    await findById(artikelId);
});

async function findById(id) {
    const dataSource = 'artikelen/' + id;
    const response = await fetch(dataSource);
    if (response.ok) {
        const artikel = await response.json();
        toon("info")
        setText("ean", artikel.ean);
        setText("naam", artikel.naam);
        setText("beschrijving", artikel.beschrijving);
        setText("gewichtInGram", artikel.gewichtInGram);
        setText("voorraad", artikel.voorraad);
    } else {
        if (response.status === 404) {
            toon("nietGevonden")
        } else {
            toon("storing")
        }
    }
}