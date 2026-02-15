package com.lmgamarra.retotecnico.controller;

import com.lmgamarra.retotecnico.dto.PriceResponseDTO;
import com.lmgamarra.retotecnico.service.PriceService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class PriceControllerTest {

    @Test
    void getPrice_returnsMono() {
        PriceService service = mock(PriceService.class);
        when(service.getPriceBySku("KEY-001"))
                .thenReturn(Mono.just(new PriceResponseDTO("KEY-001", new BigDecimal("150.00"))));

        WebTestClient client = WebTestClient.bindToController(new PriceController(service)).build();

        client.get().uri("/api/prices/KEY-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.sku").isEqualTo("KEY-001")
                .jsonPath("$.price").isEqualTo(150.00);
    }
}
