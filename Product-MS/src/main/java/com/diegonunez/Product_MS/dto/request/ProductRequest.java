package com.diegonunez.Product_MS.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotNull(message = "Product name is required")
    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 2, max = 100, message = "Product name must have between 2 and 100 characters")
    private String name;
    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.01", message = "The price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Product price must have until 2 decimals")
    private BigDecimal price;
}
