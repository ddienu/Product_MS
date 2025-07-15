package com.diegonunez.Product_MS.controller;

import com.diegonunez.Product_MS.dto.jsonapi.ProductListResponseJsonApi;
import com.diegonunez.Product_MS.dto.jsonapi.ProductResponseJsonApi;
import com.diegonunez.Product_MS.dto.request.ProductRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IProductController {

    ResponseEntity<ProductResponseJsonApi> getProductById(Integer productId);

    ResponseEntity<ProductListResponseJsonApi> getProducts(Integer page, Integer size, HttpServletRequest request);
    ResponseEntity<ProductResponseJsonApi> createProduct(ProductRequest newProduct);
    ResponseEntity<ProductResponseJsonApi> updateProduct(Integer productId, ProductRequest productUpdated);
    ResponseEntity<Boolean> deleteProduct(Integer productId);
}
