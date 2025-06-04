package com.prularia.magazijn.retour;

import com.prularia.magazijn.bestelling.AfgerondeBestellijnDTO;
import com.prularia.magazijn.bestelling.BestellijnDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/retouren")
public class RetourController {
    private final RetourService retourService;

    public RetourController(RetourService retourService) {
        this.retourService = retourService;
    }

    @PostMapping("/{bestelId}")
    public ResponseEntity<String> verwerkRetour(@PathVariable long bestelId) {
        try {
            retourService.verwerkRetour(bestelId);
            return ResponseEntity.ok("Retour verwerkt en in stock geplaatst.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fout bij verwerking van retour: " + e.getMessage());
        }
    }
}
