package com.prularia.magazijn.uitgaandeLevering;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uitgaandeLeveringen")
public class UitgaandeLeveringsController {
    private final UitgaandeLeveringsService uitgaandeLeveringsService;

    public UitgaandeLeveringsController(UitgaandeLeveringsService uitgaandeLeveringsService) {
        this.uitgaandeLeveringsService = uitgaandeLeveringsService;
    }

    @PostMapping("/retour/beschadigd/{bestelId}")
    public void updateStatusToBeschadigd(@PathVariable long bestelId) {
        uitgaandeLeveringsService.updateStatusToBeschadigd(bestelId);
    }
}
