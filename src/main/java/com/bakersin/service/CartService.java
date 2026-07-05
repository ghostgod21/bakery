package com.bakersin.service;

import com.bakersin.model.Cart;
import com.bakersin.model.CartItem;
import com.bakersin.model.Product;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * Manages the shopping cart, which lives entirely in the HTTP session — no login required,
 * and it naturally survives across page views for the duration of the visitor's session.
 */
@Service
public class CartService {

    private static final String SESSION_KEY = "cart";

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Product product, int quantity) {
        if (quantity < 1) {
            quantity = 1;
        }
        Cart cart = getCart(session);
        CartItem existing = cart.getItems().get(product.getId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem(
                    product.getId(),
                    product.getName(),
                    product.getImageUrl(),
                    product.getUnit().getLabel(),
                    product.getEffectivePrice(),
                    quantity
            );
            cart.getItems().put(product.getId(), item);
        }
    }

    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        Cart cart = getCart(session);
        if (quantity <= 0) {
            cart.getItems().remove(productId);
            return;
        }
        CartItem item = cart.getItems().get(productId);
        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    public void removeItem(HttpSession session, Long productId) {
        getCart(session).getItems().remove(productId);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }
}
