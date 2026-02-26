package com.example.shop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PricingServiceTest {

    @Test
    void calculateSubtotal_emptyOrder_returnsZero() {
        PricingService ps = new PricingService();
        Order order = new Order();
        assertEquals(0.0, ps.calculateSubtotal(order), 1e-9);
    }

    @Test
    void calculateSubtotal_multipleItems_sumsTotals() {
        PricingService ps = new PricingService();
        Order order = new Order();
        order.addItem(new OrderItem("A", 2, 10.0)); // 20
        order.addItem(new OrderItem("B", 1, 5.0));  // 5
        assertEquals(25.0, ps.calculateSubtotal(order), 1e-9);
    }



    @Test
    void calculateTax_positiveSubtotal_returns20Percent() {
        PricingService ps = new PricingService();
        assertEquals(20.0, ps.calculateTax(100.0), 1e-9);
    }

    @Test
    void calculateTax_zeroSubtotal_returnsZero() {
        PricingService ps = new PricingService();
        assertEquals(0.0, ps.calculateTax(0.0), 1e-9);
    }

    @Test
    void calculateTax_negativeSubtotal_throwsException() {
        PricingService ps = new PricingService();
        assertThrows(IllegalArgumentException.class,
                () -> ps.calculateTax(-10.0));
    }
}