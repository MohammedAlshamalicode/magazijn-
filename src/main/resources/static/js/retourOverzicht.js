"use strict";
import { byId } from "./util.js";


const voltooidButton = byId("retourVoltooid");
const tbody = byId("bestellingBody");

const bestellijnDTO = JSON.parse(sessionStorage.getItem("retourBestellijnen")) ;


// check if all checkboxes are checked
function checkAllChecked() {
    const checkboxes = tbody.querySelectorAll("input[type='checkbox']");
    const allChecked = Array.from(checkboxes).every(checkbox => checkbox.checked);
    voltooidButton.disabled = !allChecked;
}


function handleRetourCompletion() {
    alert("Alle items zijn gecontroleerd. Retour is voltooid!");
    window.location = "retour.html"; // Redirect to retour.html
}

tbody.innerHTML = "";

bestellijnDTO.forEach((bestellijn, index) => {
    const tr = tbody.insertRow();

    // Create checkbox cell
    const td = tr.insertCell();
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.id = `checkbox-${index}`;
    checkbox.addEventListener("change", checkAllChecked);

    const div = document.createElement("div");


    const locatie = document.createElement("span");
    locatie.textContent = ` ${bestellijn.locatie}, `;
    const beschrijving = document.createElement("span");
    beschrijving.textContent = bestellijn.naam;


    const aantal = document.createElement("span");
    aantal.textContent = `, x${bestellijn.aantal}`;

    td.appendChild(checkbox);
    td.appendChild(div);
    div.appendChild(locatie);
    div.appendChild(beschrijving);
    div.appendChild(aantal);
});

voltooidButton.disabled = true;
voltooidButton.addEventListener("click", handleRetourCompletion, { once: true });
