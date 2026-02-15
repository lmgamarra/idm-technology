package com.lmgamarra.retotecnico.controller;

import com.lmgamarra.retotecnico.client.PricingClient;
import com.lmgamarra.retotecnico.dto.ProductResponseDTO;
import com.lmgamarra.retotecnico.dto.ProductWithPriceResponseDTO;
import com.lmgamarra.retotecnico.entity.Product;
import com.lmgamarra.retotecnico.exception.NotFoundException;
import com.lmgamarra.retotecnico.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    private final PricingClient pricingClient;

    public ProductController(ProductService service, PricingClient pricingClient) {
        this.service = service;
        this.pricingClient = pricingClient;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProductResponseDTO> create(@RequestBody Product p) {
        return Mono.fromCallable(() -> ProductResponseDTO.from(service.create(p)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProductResponseDTO> getById(@PathVariable("id") Long id) {
        return Mono.fromCallable(() -> ProductResponseDTO.from(service.getById(id)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping(value = "/sku/{sku}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProductResponseDTO> getBySku(@PathVariable("sku") String sku) {
        return Mono.fromCallable(() -> ProductResponseDTO.from(service.getBySku(sku)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping(value = "/sku/{sku}/with-price", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProductWithPriceResponseDTO> getBySkuWithPrice(@PathVariable("sku") String sku) {
        return Mono.fromCallable(() -> service.getBySku(sku))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(product -> pricingClient.getPriceBySku(product.getSku())
                        .switchIfEmpty(Mono.error(new NotFoundException("Price not found for sku=" + product.getSku())))
                        .map(price -> ProductWithPriceResponseDTO.from(product, price))
                );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ProductResponseDTO> listActive() {
        return Mono.fromCallable(service::listActive)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .map(ProductResponseDTO::from);
    }
}
