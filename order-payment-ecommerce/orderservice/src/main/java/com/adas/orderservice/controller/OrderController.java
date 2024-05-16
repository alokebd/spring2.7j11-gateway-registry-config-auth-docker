package com.adas.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.adas.orderservice.payload.request.OrderRequest;
import com.adas.orderservice.payload.response.OrderResponse;
import com.adas.orderservice.service.OrderService;

@RestController
@RequestMapping("/order")
@Log4j2
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/placeorder")
    public ResponseEntity<Long> placeOrder(HttpServletRequest request, @RequestBody OrderRequest orderRequest) {

        log.info("OrderController | placeOrder is called");

        log.info("OrderController | placeOrder | orderRequest: {}", orderRequest.toString());
                
        String bearerToken = request.getHeader("Authorization");
        log.info("OrderController | placeOrder | Authorization (header) : " + bearerToken);
        long orderId = orderService.placeOrder(bearerToken,orderRequest);
        log.info("Order Id: {}", orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderId, @RequestHeader("Authorization") String bearerToken) {

        log.info("OrderController | getOrderDetails is called for order id:"+orderId);

        log.info("OrderController | getOrderDetails | Authorization (header) : " + bearerToken);
    	
        OrderResponse orderResponse
                = orderService.getOrderDetails(orderId, bearerToken);

        log.info("OrderController | getOrderDetails | orderResponse : " + orderResponse.toString());

        return new ResponseEntity<>(orderResponse,
                HttpStatus.OK);
    }
}
