package com.diegonunez.Product_MS.mapper;

import com.diegonunez.Product_MS.dto.jsonapi.ProductResponseJsonApi;
import com.diegonunez.Product_MS.entity.Product;

public class ProductJsonApiMapper {

    private ProductJsonApiMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ProductResponseJsonApi toJsonApi(Product product){
        return new ProductResponseJsonApi(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }


}
