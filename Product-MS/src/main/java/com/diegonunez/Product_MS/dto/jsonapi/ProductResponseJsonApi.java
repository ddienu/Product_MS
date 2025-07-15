package com.diegonunez.Product_MS.dto.jsonapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class ProductResponseJsonApi {

    private final Data data;

    public ProductResponseJsonApi(Integer id, String name, BigDecimal price) {
        this.data = new Data("product", id.toString(), new Attributes(name, price));
    }
    @Getter @Setter @AllArgsConstructor
    public static class Data {
        private final String type;
        private final String id;
        private final Attributes attributes;
    }

    @Getter @Setter @AllArgsConstructor
    public static class Attributes {
        private final String name;
        private final BigDecimal price;
    }

}
