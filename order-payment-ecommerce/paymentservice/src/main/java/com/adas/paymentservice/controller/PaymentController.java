package com.adas.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.adas.paymentservice.payload.PaymentRequest;
import com.adas.paymentservice.payload.PaymentResponse;
import com.adas.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Log4j2
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest) {

        log.info("PaymentController | doPayment is called");

        log.info("PaymentController | doPayment | paymentRequest : " + paymentRequest.toString());

        return new ResponseEntity<>(
                paymentService.doPayment(paymentRequest),
                HttpStatus.OK
        );
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByOrderId(@PathVariable long orderId) {

        log.info("PaymentController | doPayment is called");

        log.info("PaymentController | doPayment | orderId : " + orderId);

        return new ResponseEntity<>(
                paymentService.getPaymentDetailsByOrderId(orderId),
                HttpStatus.OK
        );
    }
}
