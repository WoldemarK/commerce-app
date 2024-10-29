package com.example.product.model.product;

import java.math.BigDecimal;

public record ProductPurchaseResponse
        (
                Integer productId,
                String name,
                String description,
                BigDecimal price,
                double quantity
        ) {
}
