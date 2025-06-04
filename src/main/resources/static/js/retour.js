"use strict";
import { byId, toon, verberg } from "./util.js";

byId("confirmBtn").onclick = async (event) => {
    sessionStorage.clear();
    event.preventDefault();

    const bestelIdInput = byId("bestelId");
    const isBeschadigd = byId("beschadigd").checked;
    const bestelId = bestelIdInput.value;

    verberg("storing");
    verberg("bestelIdFout");
    verberg("retourFout");

    if (!bestelIdInput.checkValidity()) {
        toon("bestelIdFout");
        bestelIdInput.focus();
        return;
    }

    if (!(await checkBestelId(bestelId))) {
        toon("storing");
        return;
    }

    if (await isStatus6(bestelId)) {
        toon("storing");
        return;
    }

    await (isBeschadigd ? processDamagedItems(bestelId) : processNonDamagedRetour(bestelId));
};

async function checkBestelId(bestelId) {
    const response = await fetch(`bestelling/${bestelId}`);
    if (response.ok) {
        const data = await response.json();
        return data.length > 0;
    }
    return false;
}

async function isStatus6(bestelId) {
    const response = await fetch(`uitgaandeLeveringen/${bestelId}/status6`);
    if (!response.ok) {
        return false;
    }
    return await response.json();
}


async function isStatus4(bestelId) {
    const response = await fetch(`uitgaandeLeveringen/${bestelId}/status4`);
    if (!response.ok) {
        return false;
    }
    return await response.json();
}

async function processNonDamagedRetour(bestelId) {
    verberg("storing");
    verberg("bestelIdFout");
    verberg("retourFout");

    const response = await fetch(`bestelling/${bestelId}`);
    if (!response.ok) {
        toon("storing");
        return;
    }

    const bestellijnDTO = await response.json();

    const retourConfirmationResponse = await fetch(`retouren/${bestelId}`, {
        method: "POST"
    });

    if (retourConfirmationResponse.ok) {
        sessionStorage.setItem("retourBestellijnen", JSON.stringify(bestellijnDTO));
        window.location = "retourOverzicht.html";
    } else {
        toon("retourFout");
    }
}
//process damaged items
async function processDamagedItems(bestelId) {
    verberg("storing");
    verberg("bestelIdFout");

    if (await isStatus4(bestelId)) {
        toon("storing");
        return;
    }

    const response = await fetch(`uitgaandeLeveringen/retour/beschadigd/${bestelId}`, {
        method: "POST"
    });

    if (response.ok) {
        alert("De beschadigde bestelling is succesvol verwerkt en wordt in de retour container geplaatst.");
    } else {
        toon("retourFout");
    }
}
