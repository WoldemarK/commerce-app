package com.example.order.mapper;

import com.example.order.model.Order;
import com.example.order.model.OrderLine;
import com.example.order.model.OrderLineRequest;
import com.example.order.model.OrderLineResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.orderId())
                .productId(request.productId())
                .order
                        (
                                Order.builder()
                                        .id(request.orderId())
                                        .build()
                        )
                .quantity(request.quantity())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(orderLine.getId(), orderLine.getQuantity());
    }
}
