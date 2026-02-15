package com.lmgamarra.retotecnico.repository;

import com.lmgamarra.retotecnico.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {
    Optional<PriceEntity> findBySku(String sku);
}
