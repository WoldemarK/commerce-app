package com.example.order.rest;

import com.example.order.model.OrderRequest;
import com.example.order.model.OrderResponse;
import com.example.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<Integer> createOrder(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(this.service.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(this.service.findAllOrders());
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(this.service.findById(orderId));
    }
}
