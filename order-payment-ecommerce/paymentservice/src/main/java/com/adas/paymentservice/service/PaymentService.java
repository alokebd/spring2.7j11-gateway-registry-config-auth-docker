package com.adas.paymentservice.service;

import com.adas.paymentservice.payload.PaymentRequest;
import com.adas.paymentservice.payload.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(long orderId);
}
