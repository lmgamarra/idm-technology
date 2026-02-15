package com.lmgamarra.retotecnico.service;

import com.lmgamarra.retotecnico.entity.PriceEntity;
import com.lmgamarra.retotecnico.exception.NotFoundException;
import com.lmgamarra.retotecnico.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PriceServiceTest {

    @Test
    void getPriceBySku_whenMissing_emitsNotFound() {
        PriceRepository repo = mock(PriceRepository.class);
        when(repo.findBySku("X")).thenReturn(Optional.empty());

        PriceService service = new PriceService(repo);

        StepVerifier.create(service.getPriceBySku("X"))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void getPriceBySku_whenExists_returnsDto() {
        PriceRepository repo = mock(PriceRepository.class);
        when(repo.findBySku("KEY-001"))
                .thenReturn(Optional.of(new PriceEntity("KEY-001", new BigDecimal("150.00"))));

        PriceService service = new PriceService(repo);

        StepVerifier.create(service.getPriceBySku("KEY-001"))
                .expectNextMatches(dto -> dto.sku().equals("KEY-001") && dto.price().compareTo(new BigDecimal("150.00")) == 0)
                .verifyComplete();
    }
}
