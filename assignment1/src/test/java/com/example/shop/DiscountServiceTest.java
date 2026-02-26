package com.example.shop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DiscountServiceTest {

    @Test
    void applyDiscount_nullCode_returnsSubtotal() {
        DiscountService ds = new DiscountService();
        assertEquals(100.0, ds.applyDiscount(100.0, null));
    }

    @Test
    void applyDiscount_blankCode_returnsSubtotal() {
        DiscountService ds = new DiscountService();
        assertEquals(100.0, ds.applyDiscount(100.0, "   "));
    }

    @Test
    void applyDiscount_student10_applies10PercentOff_caseInsensitive() {
        DiscountService ds = new DiscountService();
        assertEquals(90.0, ds.applyDiscount(100.0, "student10"), 1e-9);
    }

    @Test
    void applyDiscount_blackFriday_applies30PercentOfSubtotal() {
        DiscountService ds = new DiscountService();
        assertEquals(70.0, ds.applyDiscount(100.0, "BLACKFRIDAY"), 1e-9);
    }

    @Test
    void applyDiscount_unknownCode_returnsSubtotal() {
        DiscountService ds = new DiscountService();
        assertEquals(100.0, ds.applyDiscount(100.0, "WELCOME"), 1e-9);
    }

    @Test
    void applyDiscount_invalidCode_throwsException() {
        DiscountService ds = new DiscountService();
        assertThrows(IllegalArgumentException.class,
            () -> ds.applyDiscount(100.0, "INVALID"));
}
}