package com.lmgamarra.retotecnico.service;

import com.lmgamarra.retotecnico.entity.Product;
import com.lmgamarra.retotecnico.exception.NotFoundException;
import com.lmgamarra.retotecnico.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
public class ProductService {

    private final ProductRepository repo;

    // Supplier (excepción custom)
    private final Supplier<RuntimeException> productNotFound =
            () -> new NotFoundException("Product not found");

    // Predicate (reglas simples)
    private final Predicate<Product> isActive = Product::isActive;

    // Consumer (auditoría simple; luego puedes reemplazar por logger)
    private final Consumer<Product> auditRead = p -> System.out.println("Read product sku=" + p.getSku());

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product create(Product p) {
        return repo.save(p);
    }

    public Product getById(Long id) {
        Product p = repo.findById(id).orElseThrow(productNotFound); // Optional
        auditRead.accept(p); // Consumer
        return p;
    }

    public Product getBySku(String sku) {
        Product p = repo.findBySku(sku).orElseThrow(() -> new NotFoundException("Product not found for sku=" + sku));
        auditRead.accept(p);
        return p;
    }

    public List<Product> listActive() {
        // Streams (filter/map/toList)
        return repo.findAll()
                .stream()
                .filter(isActive)   // Predicate
                .toList();
    }
}
