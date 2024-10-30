package com.example.order.service;

import com.example.order.clients.CustomerClient;
import com.example.order.clients.PaymentClient;
import com.example.order.clients.ProductClient;
import com.example.order.exception.BusinessException;
import com.example.order.kafka.OrderConfirmation;
import com.example.order.kafka.OrderProducer;
import com.example.order.mapper.OrderMapper;
import com.example.order.model.CustomerResponse;
import com.example.order.model.Order;
import com.example.order.model.OrderLineRequest;
import com.example.order.model.OrderRequest;
import com.example.order.model.OrderResponse;
import com.example.order.model.PaymentRequest;
import com.example.order.model.PurchaseRequest;
import com.example.order.model.PurchaseResponse;
import com.example.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper mapper;
    private final OrderRepository repository;
    private final PaymentClient paymentClient;
    private final ProductClient productClient;
    private final OrderProducer orderProducer;
    private final CustomerClient customerClient;
    private final OrderLineService orderLineService;

    @Transactional
    public Integer createOrder(OrderRequest request) {
        CustomerResponse customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        List<PurchaseResponse> purchasedProducts = productClient.purchaseProducts(request.products());

        Order order = this.repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        PaymentRequest paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAllOrders() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(()
                        -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}
