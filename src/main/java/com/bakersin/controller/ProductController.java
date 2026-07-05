package com.bakersin.controller;

import com.bakersin.model.Product;
import com.bakersin.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String category,
                        @RequestParam(required = false) String q,
                        Model model) {
        List<Product> products;

        if (q != null && !q.isBlank()) {
            products = productService.search(q.trim());
            model.addAttribute("searchTerm", q.trim());
        } else if (category != null && !category.isBlank()) {
            products = productService.findByCategorySlug(category);
            productService.findCategoryBySlug(category).ifPresent(c -> model.addAttribute("activeCategory", c));
        } else {
            products = productService.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", productService.findAllCategories());
        return "products";
    }

    @GetMapping("/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        Product product = productService.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", productService.findRelated(product, 4));
        return "product-detail";
    }
}
