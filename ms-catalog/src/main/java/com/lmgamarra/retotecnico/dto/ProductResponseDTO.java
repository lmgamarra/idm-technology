package com.lmgamarra.retotecnico.dto;

import com.lmgamarra.retotecnico.entity.Product;

/**
 * DTO inmutable (record) para exponer el producto sin filtrar detalles JPA.
 */
public record ProductResponseDTO(
        Long id,
        String sku,
        String name,
        String description,
        boolean active
) {

    public static ProductResponseDTO from(Product p) {
        return new ProductResponseDTO(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getDescription(),
                p.isActive()
        );
    }
}
