package com.diegonunez.Product_MS.dto.jsonapi;

import com.diegonunez.Product_MS.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ProductListResponseJsonApi {

    private final List<Data> data;
    private final Map<String, Object> meta;
    private final Map<String, String> links;

    public ProductListResponseJsonApi(
            List<Product> products,
            int page,
            int size,
            long totalElements,
            int totalPages,
            String basePath){
        this.data = products.stream().map(
                product -> new Data(
                        "product",
                        product.getId().toString(),
                        new Attributes(
                                product.getName(),
                                product.getPrice()
                        )
                )
        ).toList();
        this.meta = Map.of(
                "page", page,
                "size", size,
                "totalElements", totalElements,
                "totalPages", totalPages
        );
        Map<String, String> linksBuilder = new java.util.LinkedHashMap<>();
        linksBuilder.put("self", basePath + "?page=" + page + "&size=" + size);

        if (page + 1 < totalPages) {
            linksBuilder.put("next", basePath + "?page=" + (page + 1) + "&size=" + size);
        }

        if (page > 0) {
            linksBuilder.put("prev", basePath + "?page=" + (page - 1) + "&size=" + size);
        }

        this.links = linksBuilder;
    }

    @Getter @Setter @AllArgsConstructor
    public static class Data{
        private final String type;
        private final String id;
        private final Attributes attributes;
    }

    @Getter @Setter @AllArgsConstructor
    public static class Attributes{
        private final String name;
        private final BigDecimal price;
    }
}
