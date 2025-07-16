package com.diegonunez.Product_MS.dto.jsonapi;

import com.diegonunez.Product_MS.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

public class ProductListResponseJsonTest {

    @Test
    void productListResponseJsonApi(){
        //Product instantiation
        Product product = new Product();
        product.setId(1);
        product.setName("Deodorant");
        product.setPrice(new BigDecimal("65000.00"));

        //Pageable parameters
        int page = 1;
        int size = 2;
        long totalElements = 10;
        int totalPages = 5;
        String basePath = "/api/v1/products";

        List<Product> products = List.of(product);

        ProductListResponseJsonApi response = new ProductListResponseJsonApi(
                products,
                page,
                size,
                totalElements,
                totalPages,
                basePath
        );

        Assertions.assertEquals(product.getId().toString(), response.getData().get(0).getId());
        Assertions.assertEquals(product.getName(), response.getData().get(0).getAttributes().getName());
        Assertions.assertEquals(product.getPrice(), response.getData().get(0).getAttributes().getPrice());
        Assertions.assertEquals("product", response.getData().get(0).getType());
        Assertions.assertEquals(size, response.getMeta().get("size"));
        Assertions.assertEquals(page, response.getMeta().get("page"));
        Assertions.assertEquals(totalPages, response.getMeta().get("totalPages"));
        Assertions.assertEquals(totalElements, response.getMeta().get("totalElements"));
        Assertions.assertEquals(basePath+"?page=1&size=2", response.getLinks().get("self"));
        Assertions.assertEquals(basePath+"?page=2&size=2", response.getLinks().get("next"));
        Assertions.assertEquals(basePath+"?page=0&size=2", response.getLinks().get("prev"));

    }
}
