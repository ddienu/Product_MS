package com.diegonunez.Product_MS.mapper;

import com.diegonunez.Product_MS.dto.jsonapi.ProductResponseJsonApi;
import com.diegonunez.Product_MS.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductJsonApiMapperTest {

    @Test
    void toJsonApiCorrect(){
        Product product = new Product();
        product.setId(1);
        product.setName("Deodorant");
        product.setPrice(new BigDecimal("65000.00"));

        ProductResponseJsonApi toJsonApiOK = ProductJsonApiMapper.toJsonApi(product);

        Assertions.assertEquals(product.getId().toString(), toJsonApiOK.getData().getId());
        Assertions.assertEquals(product.getName(), toJsonApiOK.getData().getAttributes().getName());
        Assertions.assertEquals(product.getPrice(), toJsonApiOK.getData().getAttributes().getPrice());
        Assertions.assertEquals("product", toJsonApiOK.getData().getType());
    }

    @Test
    void mapperInstantiationException() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<ProductJsonApiMapper> constructor = ProductJsonApiMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertEquals(UnsupportedOperationException.class, cause.getClass());
        assertEquals("Utility class", cause.getMessage());
    }
}
