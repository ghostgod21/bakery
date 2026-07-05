package com.bakersin.service;

import com.bakersin.dto.CheckoutForm;
import com.bakersin.model.Cart;
import com.bakersin.model.CartItem;
import com.bakersin.model.Order;
import com.bakersin.model.OrderItem;
import com.bakersin.model.OrderStatus;
import com.bakersin.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final Random random = new Random();

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order placeOrder(CheckoutForm form, Cart cart) {
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomerName(form.getName());
        order.setEmail(form.getEmail());
        order.setPhone(form.getPhone());
        order.setAddress(form.getAddress());
        order.setCity(form.getCity());
        order.setPin(form.getPin());
        order.setPaymentMethod(form.getPaymentMethod());
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());
        order.setSubtotal(cart.getSubtotal());
        order.setDeliveryFee(cart.getDeliveryFee());
        order.setTotalAmount(cart.getTotal());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems().values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    private String generateOrderNumber() {
        long timestampPart = System.currentTimeMillis() % 1_000_000L;
        int randomPart = 100 + random.nextInt(900);
        return "BKI" + timestampPart + randomPart;
    }
}
