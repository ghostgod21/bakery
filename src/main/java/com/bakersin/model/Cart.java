package com.bakersin.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The shopper's cart, stored as a single attribute in the HTTP session.
 * Keyed by product id so repeated adds of the same product just bump the quantity.
 */
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final BigDecimal FREE_DELIVERY_THRESHOLD = new BigDecimal("499");
    public static final BigDecimal DELIVERY_FEE = new BigDecimal("40");

    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    public Map<Long, CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        return items.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal getSubtotal() {
        return items.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getDeliveryFee() {
        if (isEmpty() || getSubtotal().compareTo(FREE_DELIVERY_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        return DELIVERY_FEE;
    }

    public BigDecimal getTotal() {
        return getSubtotal().add(getDeliveryFee());
    }

    public BigDecimal getAmountToFreeDelivery() {
        BigDecimal remaining = FREE_DELIVERY_THRESHOLD.subtract(getSubtotal());
        return remaining.compareTo(BigDecimal.ZERO) > 0 ? remaining : BigDecimal.ZERO;
    }

    public boolean isFreeDeliveryUnlocked() {
        return !isEmpty() && getSubtotal().compareTo(FREE_DELIVERY_THRESHOLD) >= 0;
    }

    public boolean isDeliveryFeeCharged() {
        return getDeliveryFee().compareTo(BigDecimal.ZERO) > 0;
    }
}
