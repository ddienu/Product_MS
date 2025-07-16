package com.diegonunez.Product_MS.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductTest {

    private Product product;

    @BeforeEach
    void setup(){
        product = new Product();
        product.setId(1);
        product.setName("Deodorant");
        product.setPrice(new BigDecimal("65000.00"));
    }

    @Test
    void EntityGettersSettersWorksFine(){
        Assertions.assertEquals(1, product.getId());
        Assertions.assertEquals("Deodorant", product.getName());
        Assertions.assertEquals(new BigDecimal("65000.00"), product.getPrice());
    }

    @Test
    void EntityWorksOK(){
        Assertions.assertNotEquals(2, product.getId());
        Assertions.assertNotEquals("Shampoo", product.getName());
        Assertions.assertNotEquals(new BigDecimal("65100.00"), product.getPrice());Assertions.assertNotEquals(2, product.getId());
    }

    @Test
    void EntityEmptyObject(){
        Product product2 = new Product();
        Assertions.assertNull(product2.getId());
        Assertions.assertNull(product2.getName());
        Assertions.assertNull(product2.getPrice());
    }


}
