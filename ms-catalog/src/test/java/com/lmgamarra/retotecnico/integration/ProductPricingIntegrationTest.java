package com.lmgamarra.retotecnico.integration;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductPricingIntegrationTest {

    private static MockWebServer pricingServer;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    static void startMockServer() throws Exception {
        pricingServer = new MockWebServer();
        pricingServer.start();
    }

    @AfterAll
    static void stopMockServer() throws Exception {
        pricingServer.shutdown();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("pricing.base-url", () -> pricingServer.url("/").toString());
    }

    @Test
    void catalogCallsPricingAndReturnsComposedResponse() {
        pricingServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("{\"sku\":\"KEY-001\",\"price\":150.00}"));

        webTestClient.get()
                .uri("/api/products/sku/KEY-001/with-price")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.sku").isEqualTo("KEY-001")
                .jsonPath("$.price").isEqualTo(150.00);
    }
}
