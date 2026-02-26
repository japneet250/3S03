package com.example.shop;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PaymentValidatorTest {

   

    @Test
    void isPaymentMethodValid_card_returnsTrue() {
        PaymentValidator pv = new PaymentValidator();
        assertTrue(pv.isPaymentMethodValid("card"));
    }

    @Test
    void isPaymentMethodValid_paypal_returnsTrue() {
        PaymentValidator pv = new PaymentValidator();
        assertTrue(pv.isPaymentMethodValid("paypal"));
    }

    @Test
    void isPaymentMethodValid_crypto_returnsFalse() {
        PaymentValidator pv = new PaymentValidator();
        assertFalse(pv.isPaymentMethodValid("crypto"));
    }

    @Test
    void isPaymentMethodValid_null_returnsFalse() {
        PaymentValidator pv = new PaymentValidator();
        assertFalse(pv.isPaymentMethodValid(null));
    }

    @Test
    void isPaymentMethodValid_unknownMethod_throwsException() {
        PaymentValidator pv = new PaymentValidator();
        assertThrows(UnsupportedOperationException.class,
                () -> pv.isPaymentMethodValid("banktransfer"));
    }   
}