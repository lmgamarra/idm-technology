package com.lmgamarra.retotecnico.service;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.lmgamarra.retotecnico.dto.PriceResponseDTO;
import com.lmgamarra.retotecnico.entity.PriceEntity;
import com.lmgamarra.retotecnico.exception.NotFoundException;
import com.lmgamarra.retotecnico.repository.PriceRepository;

@Service
public class PriceService {

    private final PriceRepository repo;

    private final Supplier<RuntimeException> priceNotFound =
            () -> new NotFoundException("Price not found");

    public PriceService(PriceRepository repo) {
        this.repo = repo;
    }

    public Mono<PriceResponseDTO> getPriceBySku(String sku) {
        // JPA es bloqueante: lo llevamos a boundedElastic y lo exponemos como Mono.
        return Mono.fromCallable(() -> repo.findBySku(sku)
                        .map(this::toDto)
                        .orElseThrow(priceNotFound))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private PriceResponseDTO toDto(PriceEntity e) {
        return new PriceResponseDTO(e.getSku(), e.getPrice());
    }
}
