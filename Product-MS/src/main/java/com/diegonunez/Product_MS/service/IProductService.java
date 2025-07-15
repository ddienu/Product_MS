package com.diegonunez.Product_MS.service;

import com.diegonunez.Product_MS.dto.jsonapi.ProductListResponseJsonApi;
import com.diegonunez.Product_MS.dto.jsonapi.ProductResponseJsonApi;
import com.diegonunez.Product_MS.dto.request.ProductRequest;
import org.springframework.data.domain.Pageable;


public interface IProductService {
    ProductResponseJsonApi getProductById(Integer productId);
    ProductListResponseJsonApi getProducts(Pageable pageable, String basePath);
    ProductResponseJsonApi createNewProduct(ProductRequest newProduct);
    ProductResponseJsonApi updateProduct(Integer productId, ProductRequest productUpdated);
    Boolean deleteProduct(Integer productId);
}
