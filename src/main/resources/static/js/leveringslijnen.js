"use strict"
import {byId, toon, verberg} from "./util.js";

const leveringsId = localStorage.getItem("leveringsId");
let leveringslijnen;
if (leveringsId != null) {
    byId("leveringsId").textContent = leveringsId;
    leveringslijnen = await getLeveringslijnen(leveringsId);
    verwerkLeveringslijnen(leveringslijnen);
} else {
    verberg("storing");
    toon("geenLeveringen");
    verberg("leveringVoltooidButton");
}


async function getLeveringslijnen(leveringsId) {
    // juiste url is inkomendeleveringslijnen?
    const response = await fetch(`inkomendeleveringslijnen/${leveringsId}`);
    if (response.ok) {
        verberg("storing");
        verberg("geenLeveringen");
        toon("leveringVoltooidButton");
        return  await response.json();
    } else if (response.status === 404) {
        verberg("storing");
        toon("geenLeveringen");
        verberg("leveringVoltooidButton");
    } else {
        toon("storing");
        verberg("geenLeveringen");
        verberg("leveringVoltooidButton");
    }
}

function verwerkLeveringslijnen(leveringslijnen) {
    const table = byId("leveringBody");
    for (const leveringslijn of leveringslijnen) {
        const tr = table.insertRow();

        // Checkbox
        const checkboxCell = tr.insertCell();
        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkboxCell.appendChild(checkbox);
        checkbox.onchange = () => {
            if (checkbox.checked) {
                tr.classList.add("checked");
            } else {
                tr.classList.remove("checked");
            }
            localStorage.setItem(`leverCheckbox${leveringslijn.artikelId}`, `${checkbox.checked}`);
        };
        if (localStorage.getItem(`leverCheckbox${leveringslijn.artikelId}`) === "true") {
            checkbox.checked = true;
            tr.classList.add("checked");
        }


        // locatie
        tr.insertCell().textContent = leveringslijn.plaats;

        // artikel naam
        const naamCell = tr.insertCell();
        const link = document.createElement("a");
        link.href = "#";
        link.onclick = function () {
            sessionStorage.setItem("artikelId", leveringslijn.artikelId);
            window.open("detail.html");
        };
        link.textContent = leveringslijn.naam;
        naamCell.appendChild(link);

        // aantal
        tr.insertCell().textContent = `x${leveringslijn.aantalGoedgekeurd}`;
    }

    // Als alle checkboxen al aangevinkt waren wanneer de pagina geladen werd, wordt de "Levering voltooid" knop actief
    const bestellijnCheckboxen = document.querySelectorAll('input[type="checkbox"]');

    const alleCheckboxenAangevinkt = Array.from(bestellijnCheckboxen).every(checkbox => checkbox.checked);
    byId("leveringVoltooidButton").disabled = !alleCheckboxenAangevinkt;
}

// eventListener om wijzigingen in de leveringslijn-checkboxen na te kijken
byId("main").addEventListener("change", () => {
    const bestellijnCheckboxen = document.querySelectorAll('input[type="checkbox"]');

    const alleCheckboxenAangevinkt = Array.from(bestellijnCheckboxen).every(checkbox => checkbox.checked);
    byId("leveringVoltooidButton").disabled = !alleCheckboxenAangevinkt;
});

byId("leveringVoltooidButton").addEventListener("click", async () => {
    if (leveringsId) {
        await fetch(`magazijnplaats/aanvullenInRek`, {
            method: "POST",
            body: JSON.stringify(leveringslijnen),
            headers: {
                "Content-Type": "application/json"
            }
        });
    }
    localStorage.clear();
    sessionStorage.clear();
    window.location = "leverbon.html"
})