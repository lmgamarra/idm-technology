package com.lmgamarra.retotecnico.service;

import com.lmgamarra.retotecnico.entity.Product;
import com.lmgamarra.retotecnico.exception.NotFoundException;
import com.lmgamarra.retotecnico.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private final ProductRepository repo = mock(ProductRepository.class);
    private final ProductService service = new ProductService(repo);

    @Test
    void getById_whenMissing_throwsNotFound() {
        when(repo.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(10L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void listActive_filtersOnlyActive() {
        Product a = new Product("A1","A","x"); a.setActive(true);
        Product b = new Product("B1","B","x"); b.setActive(false);
        when(repo.findAll()).thenReturn(List.of(a,b));

        List<Product> result = service.listActive();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSku()).isEqualTo("A1");
    }
}
