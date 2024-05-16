package com.adas.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adas.paymentservice.model.TransactionDetails;

import java.util.Optional;

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {

    Optional<TransactionDetails> findByOrderId(long orderId);
}
