package com.example.payment.service;

import com.example.payment.mapper.PaymentMapper;
import com.example.payment.model.Payment;
import com.example.payment.model.PaymentRequest;
import com.example.payment.notification.NotificationProducer;
import com.example.payment.notification.PaymentNotificationRequest;
import com.example.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper mapper;
    private final PaymentRepository repository;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest request) {
        Payment payment = this.repository.save(this.mapper.toPayment(request));

        this.notificationProducer.sendNotification
                (
                        new PaymentNotificationRequest(
                                request.orderReference(),
                                request.amount(),
                                request.paymentMethod(),
                                request.customer().firstname(),
                                request.customer().lastname(),
                                request.customer().email()
                        )
                );
        return payment.getId();
    }
}
