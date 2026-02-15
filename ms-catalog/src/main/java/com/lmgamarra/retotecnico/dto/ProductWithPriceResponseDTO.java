package com.lmgamarra.retotecnico.dto;

import com.lmgamarra.retotecnico.entity.Product;

import java.math.BigDecimal;

public record ProductWithPriceResponseDTO(
        Long id,
        String sku,
        String name,
        String description,
        boolean active,
        BigDecimal price
) {

    public static ProductWithPriceResponseDTO from(Product p, PriceResponseDTO price) {
        return new ProductWithPriceResponseDTO(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getDescription(),
                p.isActive(),
                price.price()
        );
    }
}
