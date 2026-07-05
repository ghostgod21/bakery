package com.bakersin.service;

import com.bakersin.model.Category;
import com.bakersin.model.Product;
import com.bakersin.repository.CategoryRepository;
import com.bakersin.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    public List<Product> findByCategorySlug(String categorySlug) {
        return productRepository.findByCategorySlug(categorySlug);
    }

    public Optional<Product> findBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    public List<Product> search(String query) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }

    public List<Product> findFeaturedOnSale(int limit) {
        return productRepository.findBySalePriceIsNotNull().stream()
                .filter(Product::isOnSale)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Product> findRelated(Product product, int limit) {
        return productRepository.findByCategorySlug(product.getCategory().getSlug()).stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
