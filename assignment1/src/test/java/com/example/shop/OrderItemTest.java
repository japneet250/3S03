
package com.example.shop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void constructor_validInputs_setsFieldsAndTotalPriceWorks() {
        OrderItem item = new OrderItem("Book", 2, 15.5);
        assertEquals(31.0, item.getTotalPrice(), 1e-9);
        assertEquals(2, item.getQuantity());
    }
}