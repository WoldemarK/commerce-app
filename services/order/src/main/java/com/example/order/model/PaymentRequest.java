package com.example.order.model;

import java.math.BigDecimal;

public record PaymentRequest
        (
                BigDecimal amount,
                PaymentMethod paymentMethod,
                Integer orderId,
                String orderReference,
                CustomerResponse customer
        ) {
}
