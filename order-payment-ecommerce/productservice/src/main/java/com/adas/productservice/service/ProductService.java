package com.adas.productservice.service;

import com.adas.productservice.payload.request.ProductRequest;
import com.adas.productservice.payload.response.ProductResponse;

public interface ProductService {

    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);

    public void deleteProductById(long productId);
}
