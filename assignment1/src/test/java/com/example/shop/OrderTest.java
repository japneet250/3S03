package com.example.shop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void addItem_afterOrderProcessed_throwsException() {
        Order order = new Order();
        order.addItem(new OrderItem("A", 1, 10.0));
        order.setStatus(OrderStatus.PAID);

        assertThrows(IllegalStateException.class,
                () -> order.addItem(new OrderItem("B", 1, 5.0)));
    }
}