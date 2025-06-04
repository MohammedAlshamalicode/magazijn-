"use strict";
import {byId, toon, verberg} from "./util.js";

document.addEventListener("DOMContentLoaded", async () => {
    verwerkAantalBestellingen();
});

async function verwerkAantalBestellingen() {
    const response = await fetch("bestellingen/aantal");
    if (response.ok) {
        toon("main");
        verberg("storing");
        byId("aantalBestellingen").textContent = await response.text();
    } else {
        toon("storing");
        verberg("main");
        setTimeout(verwerkAantalBestellingen, 5000);
    }
    await verwerkBestellingen();
}

async function verwerkBestellingen() {
    const bestellingen = await getBestellingen();
    const table = byId("bestellingBody");
    table.innerHTML = "";
    for (const bestelling of bestellingen) {
        const tr = table.insertRow();
        tr.insertCell().textContent = bestelling.bestelId;
        tr.insertCell().textContent = bestelling.aantalProducten;
        tr.insertCell().textContent = `${bestelling.totaleGewicht.toFixed(2)} kg`;
    }

    // Timer: elke 60 seconden start dit opnieuw
    setTimeout(verwerkAantalBestellingen, 60000);
}

async function getBestellingen() {
    const response = await fetch("bestellingen");
    if (response.ok) {
        toon("main");
        verberg("storing");
        return response.json();
    } else {
        toon("storing");
        verberg("main");
        setTimeout(verwerkAantalBestellingen, 5000);
    }
}