package com.example.order.model;

public record OrderLineRequest
        (
                Integer id,
                Integer orderId,
                Integer productId,
                double quantity
        ) {
}
