package com.bakersin.controller;

import com.bakersin.model.Product;
import com.bakersin.service.CartService;
import com.bakersin.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductRepository productRepository;

    public CartController(CartService cartService, ProductRepository productRepository) {
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String view(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        return "cart";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long productId,
                       @RequestParam(defaultValue = "1") int quantity,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        cartService.addToCart(session, product, quantity);
        redirectAttributes.addFlashAttribute("message", product.getName() + " added to your cart");
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long productId,
                          @RequestParam int quantity,
                          HttpSession session) {
        cartService.updateQuantity(session, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long productId, HttpSession session) {
        cartService.removeItem(session, productId);
        return "redirect:/cart";
    }
}
