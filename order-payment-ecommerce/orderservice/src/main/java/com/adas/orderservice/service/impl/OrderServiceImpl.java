package com.adas.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adas.orderservice.exception.CustomException;
import com.adas.orderservice.external.client.PaymentService;
import com.adas.orderservice.external.client.ProductService;
import com.adas.orderservice.model.Order;
import com.adas.orderservice.payload.request.OrderRequest;
import com.adas.orderservice.payload.request.PaymentRequest;
import com.adas.orderservice.payload.response.OrderResponse;
import com.adas.orderservice.payload.response.PaymentResponse;
import com.adas.orderservice.payload.response.ProductResponse;
import com.adas.orderservice.repository.OrderRepository;
import com.adas.orderservice.service.OrderService;
import org.springframework.util.StringUtils;
import java.time.Instant;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    private final PaymentService paymentService;

    private final RestTemplate restTemplate;

    @Override
    public long placeOrder(String token, OrderRequest orderRequest) {

        log.info("OrderServiceImpl | placeOrder is called");

        //Order Entity -> Save the data with Status Order Created
        //Product Service - Block Products (Reduce the Quantity)
        //Payment Service -> Payments -> Success-> COMPLETE, Else
        //CANCELLED

        log.info("OrderServiceImpl | placeOrder | Placing Order Request orderRequest : " + orderRequest.toString());

        log.info("OrderServiceImpl | placeOrder | Calling productService through FeignClient");
        
        log.info("OrderServiceImpl | placeOrder | Authorization (Header): " + token);
    	if (StringUtils.hasText(token) && !token.startsWith("Bearer ")) {
    		token="Bearer ".concat(token);
    	 	log.info("OrderServiceImpl | placeOrder | Bearer Token: " + token);
    	}
                   
        productService.reduceQuantity(token,orderRequest.getProductId(), orderRequest.getQuantity());
   	   
        log.info("OrderServiceImpl | placeOrder | Creating Order with Status CREATED");
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);

        log.info("OrderServiceImpl | placeOrder | Calling Payment Service to complete the payment");

        PaymentRequest paymentRequest
                = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;

        try {
            paymentService.doPayment(token,paymentRequest);
            log.info("OrderServiceImpl | placeOrder | Payment done Successfully. Changing the Oder status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("OrderServiceImpl | placeOrder | Error occurred in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);

        orderRepository.save(order);

        log.info("OrderServiceImpl | placeOrder | Order Places successfully with Order Id: {}", order.getId());

        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId, String bearerToken) {
    	log.info("OrderServiceImpl | getOrderDetails | bearerToken :" + bearerToken);
    	
    	if (StringUtils.hasText(bearerToken) && !bearerToken.startsWith("Bearer ")) {
    		bearerToken="Bearer ".concat(bearerToken);
    	 	log.info("OrderServiceImpl | getOrderDetails | Bearer Token :" + bearerToken);
    	}
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        log.info("OrderServiceImpl | getOrderDetails | Get order details for Order Id : {}", orderId);

        Order order
                = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found for the order Id:" + orderId,
                        "NOT_FOUND",
                        404));

        log.info("OrderServiceImpl | getOrderDetails | Invoking Product service to fetch the product for id: {}", order.getProductId());
        /*ProductResponse productResponse
                = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class
        );*/

        ResponseEntity<ProductResponse> responseProduct = restTemplate.exchange(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                HttpMethod.GET, request, ProductResponse.class);
        ProductResponse productResponse = responseProduct.getBody();
        log.info("OrderServiceImpl | getOrderDetails | Invoking Product service (RestTemaplate) product: {}", productResponse);
        
        log.info("OrderServiceImpl | getOrderDetails | Getting payment information form the payment Service");
        /*PaymentResponse paymentResponse
                = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentResponse.class
        );*/

        ResponseEntity<PaymentResponse> responsePayment = restTemplate.exchange(
                "http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                HttpMethod.GET, request, PaymentResponse.class);
        PaymentResponse paymentResponse = responsePayment.getBody();

        log.info("OrderServiceImpl | getOrderDetails | Payment info (RestTemaplate): {}", paymentResponse);
          
        OrderResponse.ProductDetails productDetails
                = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

        OrderResponse.PaymentDetails paymentDetails
                = OrderResponse.PaymentDetails
                .builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        OrderResponse orderResponse
                = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();

        log.info("OrderServiceImpl | getOrderDetails | orderResponse : " + orderResponse.toString());

        return orderResponse;
    }
}
