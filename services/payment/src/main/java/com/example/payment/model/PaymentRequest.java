package com.example.payment.model;

import java.math.BigDecimal;

public record PaymentRequest
        (
                Integer id,
                BigDecimal amount,
                PaymentMethod paymentMethod,
                Integer orderId,
                String orderReference,
                Customer customer
        ) {
}
