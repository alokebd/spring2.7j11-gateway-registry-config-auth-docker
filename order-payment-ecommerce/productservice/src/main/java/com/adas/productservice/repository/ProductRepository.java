package com.adas.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adas.productservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
