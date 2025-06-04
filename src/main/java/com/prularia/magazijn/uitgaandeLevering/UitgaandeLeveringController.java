package com.prularia.magazijn.uitgaandeLevering;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("uitgaandeLeveringen")
public class UitgaandeLeveringController {
    private final UitgaandeLeveringService uitgaandeLeveringService;

    public UitgaandeLeveringController(UitgaandeLeveringService uitgaandeLeveringService) {
        this.uitgaandeLeveringService = uitgaandeLeveringService;
    }

    @GetMapping("{bestelId}/status6")
    public boolean heeftStatus6(@PathVariable long bestelId) {
        return uitgaandeLeveringService.heeftStatus6(bestelId);
    }
    @GetMapping("{bestelId}/status4")
    public boolean heeftStatus4(@PathVariable long bestelId) {
        return uitgaandeLeveringService.heeftStatus4(bestelId);
    }
}
