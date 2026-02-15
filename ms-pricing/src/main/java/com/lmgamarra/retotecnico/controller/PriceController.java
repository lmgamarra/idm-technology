package com.lmgamarra.retotecnico.controller;

import org.springframework.web.bind.annotation.*;

import com.lmgamarra.retotecnico.dto.PriceResponseDTO;
import com.lmgamarra.retotecnico.service.PriceService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/{sku}")
    public Mono<PriceResponseDTO> getPrice(@PathVariable String sku) {
        return priceService.getPriceBySku(sku);
    }
}
