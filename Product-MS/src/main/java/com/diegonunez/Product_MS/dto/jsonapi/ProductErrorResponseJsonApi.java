package com.diegonunez.Product_MS.dto.jsonapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
public class ProductErrorResponseJsonApi {

    private final List<ErrorItem> errors;

    public ProductErrorResponseJsonApi(String status, String title, String detail){
        this.errors = List.of(new ErrorItem(status, title, detail));
    }

    public ProductErrorResponseJsonApi(List<ErrorItem> errors){
        this.errors = errors;
    }

    @Getter @Setter @AllArgsConstructor
    public static class ErrorItem{
        private String status;
        private String title;
        private String detail;
    }
}
