package com.bakersin.controller;

import com.bakersin.dto.CheckoutForm;
import com.bakersin.model.Cart;
import com.bakersin.model.Order;
import com.bakersin.service.CartService;
import com.bakersin.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        if (!model.containsAttribute("checkoutForm")) {
            model.addAttribute("checkoutForm", new CheckoutForm());
        }
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@Valid @ModelAttribute("checkoutForm") CheckoutForm checkoutForm,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model) {
        Cart cart = cartService.getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("cart", cart);
            return "checkout";
        }

        Order order = orderService.placeOrder(checkoutForm, cart);
        cartService.clearCart(session);
        return "redirect:/order-confirmation/" + order.getOrderNumber();
    }

    @GetMapping("/order-confirmation/{orderNumber}")
    public String confirmation(@PathVariable String orderNumber, Model model) {
        Order order = orderService.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        model.addAttribute("order", order);
        return "order-confirmation";
    }
}
