package com.lmgamarra.retotecnico.dto;

import java.math.BigDecimal;

/**
 * Contrato HTTP esperado desde ms-pricing.
 */
public record PriceResponseDTO(
        String sku,
        BigDecimal price
) {}
