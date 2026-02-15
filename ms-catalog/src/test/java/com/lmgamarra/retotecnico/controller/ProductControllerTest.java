package com.lmgamarra.retotecnico.controller;

import com.lmgamarra.retotecnico.client.PricingClient;
import com.lmgamarra.retotecnico.entity.Product;
import com.lmgamarra.retotecnico.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Test
    void listActive_returnsFlux() {
        ProductService service = mock(ProductService.class);
        PricingClient pricingClient = mock(PricingClient.class);
        when(service.listActive()).thenReturn(List.of(new Product("A1","A","x")));

        WebTestClient client = WebTestClient.bindToController(new ProductController(service, pricingClient)).build();

        client.get().uri("/api/products")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].sku").isEqualTo("A1");
    }
}
