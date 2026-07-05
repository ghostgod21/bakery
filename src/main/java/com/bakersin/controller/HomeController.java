package com.bakersin.controller;

import com.bakersin.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", productService.findAllCategories());
        model.addAttribute("featuredProducts", productService.findFeaturedOnSale(8));
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
