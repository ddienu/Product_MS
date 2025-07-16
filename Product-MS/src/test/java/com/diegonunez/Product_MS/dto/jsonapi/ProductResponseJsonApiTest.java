package com.diegonunez.Product_MS.dto.jsonapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductResponseJsonApiTest {

    @Test
    void productResponseJsonApiCreatesCorrect(){
        Integer id = 1;
        String name = "Deodorant";
        BigDecimal price = new BigDecimal("65000.00");

        ProductResponseJsonApi response = new ProductResponseJsonApi(
                id,
                name,
                price
        );

        Assertions.assertEquals("product", response.getData().getType());
        Assertions.assertEquals("1", response.getData().getId());
        Assertions.assertEquals("Deodorant", response.getData().getAttributes().getName());
        Assertions.assertEquals(new BigDecimal("65000.00"), response.getData().getAttributes().getPrice());
    }
}
