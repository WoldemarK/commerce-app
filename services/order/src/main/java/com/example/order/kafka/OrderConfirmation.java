package com.example.order.kafka;

import com.example.order.model.CustomerResponse;
import com.example.order.model.PaymentMethod;
import com.example.order.model.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation
        (
                String orderReference,
                BigDecimal totalAmount,
                PaymentMethod paymentMethod,
                CustomerResponse customer,
                List<PurchaseResponse> product
        ) {
}
