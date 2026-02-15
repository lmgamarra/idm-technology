package com.lmgamarra.retotecnico.controller;

import com.lmgamarra.retotecnico.client.PricingClient;
import com.lmgamarra.retotecnico.dto.PriceResponseDTO;
import com.lmgamarra.retotecnico.entity.Product;
import com.lmgamarra.retotecnico.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class ProductWithPriceControllerTest {

    @Test
    void getBySkuWithPrice_returnsProductAndPrice() {
        ProductService service = mock(ProductService.class);
        PricingClient pricingClient = mock(PricingClient.class);

        Product p = new Product("KEY-001", "Keyboard", "Mechanical");
        when(service.getBySku("KEY-001")).thenReturn(p);
        when(pricingClient.getPriceBySku("KEY-001"))
                .thenReturn(Mono.just(new PriceResponseDTO("KEY-001", new BigDecimal("150.00"))));

        WebTestClient client = WebTestClient.bindToController(new ProductController(service, pricingClient)).build();

        client.get().uri("/api/products/sku/KEY-001/with-price")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.sku").isEqualTo("KEY-001")
                .jsonPath("$.price").isEqualTo(150.00);
    }
}
