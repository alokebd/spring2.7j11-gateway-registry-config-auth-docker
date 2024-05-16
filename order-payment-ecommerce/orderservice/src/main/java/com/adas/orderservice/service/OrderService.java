package com.adas.orderservice.service;

import com.adas.orderservice.payload.request.OrderRequest;
import com.adas.orderservice.payload.response.OrderResponse;

public interface OrderService {
    long placeOrder(String authorizationHeader,OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId, String bearerToken);
}
