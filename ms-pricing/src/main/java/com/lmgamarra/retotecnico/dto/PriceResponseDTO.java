package com.lmgamarra.retotecnico.dto;

import java.math.BigDecimal;

public record PriceResponseDTO(
        String sku,
        BigDecimal price
) {}
