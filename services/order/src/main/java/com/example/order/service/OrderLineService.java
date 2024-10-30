package com.example.order.service;

import com.example.order.mapper.OrderLineMapper;
import com.example.order.model.OrderLine;
import com.example.order.model.OrderLineRequest;
import com.example.order.model.OrderLineResponse;
import com.example.order.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineMapper mapper;
    private final OrderLineRepository repository;

    public Integer saveOrderLine(OrderLineRequest request) {
        OrderLine order = mapper.toOrderLine(request);
        return repository.save(order)
                .getId();
    }

    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return repository.findAllByOrderId(orderId)
                .stream()
                .map(mapper::toOrderLineResponse)
                .collect(Collectors.toList());
    }
}
