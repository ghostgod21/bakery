package com.bakersin.controller;

import com.bakersin.model.Category;
import com.bakersin.repository.CategoryRepository;
import com.bakersin.service.CartService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Supplies data every page's navigation bar needs (category list, cart item count)
 * without every controller having to fetch it individually.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final CategoryRepository categoryRepository;
    private final CartService cartService;

    public GlobalModelAdvice(CategoryRepository categoryRepository, CartService cartService) {
        this.categoryRepository = categoryRepository;
        this.cartService = cartService;
    }

    @ModelAttribute("navCategories")
    public List<Category> navCategories() {
        return categoryRepository.findAll();
    }

    @ModelAttribute("cartItemCount")
    public int cartItemCount(HttpSession session) {
        return cartService.getCart(session).getItemCount();
    }
}
