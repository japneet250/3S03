package com.example.shop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    @Test
    void processOrder_validPayment_marksPaid_andReturnsTotalPlusTax() {
        OrderService os = new OrderService();
        Order order = new Order();
        order.addItem(new OrderItem("A", 2, 10.0)); // subtotal 20

        double total = os.processOrder(order, "STUDENT10", "card");
        // discounted: 18, tax: 3.6, total: 21.6
        assertEquals(21.6, total, 1e-9);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void processOrder_invalidPayment_marksCancelled_andReturnsZero() {
        OrderService os = new OrderService();
        Order order = new Order();
        order.addItem(new OrderItem("A", 1, 10.0));

        double total = os.processOrder(order, "STUDENT10", "crypto");
        assertEquals(0.0, total, 1e-9);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }
}