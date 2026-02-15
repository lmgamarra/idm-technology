package com.lmgamarra.retotecnico.client;

import com.lmgamarra.retotecnico.dto.PriceResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class PricingClient {

    private final WebClient pricingWebClient;

    public PricingClient(WebClient pricingWebClient) {
        this.pricingWebClient = pricingWebClient;
    }

    public Mono<PriceResponseDTO> getPriceBySku(String sku) {
        return pricingWebClient.get()
                .uri("/api/prices/{sku}", sku)
                .retrieve()
                .bodyToMono(PriceResponseDTO.class)
                // Si el ms-pricing responde 404, lo dejamos como Mono.empty() para que el caller decida.
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.empty());
    }
}
