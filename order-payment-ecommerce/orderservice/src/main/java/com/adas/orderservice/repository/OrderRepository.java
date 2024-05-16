package com.adas.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adas.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
