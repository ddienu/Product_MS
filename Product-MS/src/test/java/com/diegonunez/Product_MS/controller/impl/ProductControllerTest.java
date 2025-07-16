package com.diegonunez.Product_MS.controller.impl;

import com.diegonunez.Product_MS.dto.jsonapi.ProductListResponseJsonApi;
import com.diegonunez.Product_MS.dto.jsonapi.ProductResponseJsonApi;
import com.diegonunez.Product_MS.dto.request.ProductRequest;
import com.diegonunez.Product_MS.entity.Product;
import com.diegonunez.Product_MS.service.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${security.api-key}")
    private String secretKey;

    private Product product;

    @BeforeEach
    void setup(){
        product = new Product(
                1,
                "Shampoo",
                BigDecimal.valueOf(15000)
        );
    }


    @Test
    void createProductSuccess() throws Exception{
        ProductRequest newProduct = new ProductRequest(
                "Shampoo",
                BigDecimal.valueOf(18000)
        );

        ProductResponseJsonApi response = new ProductResponseJsonApi(
                1,
                newProduct.getName(),
                newProduct.getPrice()
        );

        when(productService.createNewProduct(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct))
                        .header("X-API-KEY", secretKey))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.attributes.name").value("Shampoo"))
                .andExpect(jsonPath("$.data.attributes.price").value(18000));
    }

    @Test
    void createProductResponseIfNameIsEmptyOrNull() throws Exception {
        ProductRequest invalidRequest = new ProductRequest(
                null,
                BigDecimal.valueOf(18000)
        );

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .header("X-API-KEY", secretKey))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].detail").value("name: Product name is required"))
                .andExpect(jsonPath("$.errors[1].detail").value("name: Product name cannot be empty"));
    }

    @Test
    void createProductResponseIfPriceIsEmptyOrNull() throws Exception {
        ProductRequest invalidRequest = new ProductRequest(
                "Shampoo",
                null
        );

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .header("X-API-KEY", secretKey))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].detail").value("price: Product price is required"));
    }
    @Test
    void getProductByIdSuccess() throws Exception {
        Integer productId = 1;

        ProductResponseJsonApi serviceResponse = new ProductResponseJsonApi(
                product.getId(),
                product.getName(),
                product.getPrice()
        );

        when(productService.getProductById(productId)).thenReturn(serviceResponse);

        mockMvc.perform(get("/api/v1/products/{productId}", productId)
                .header("X-API-KEY", secretKey)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("product"))
                .andExpect(jsonPath("$.data.id").value(product.getId().toString()))
                .andExpect(jsonPath("$.data.attributes.name").value(product.getName()))
                .andExpect(jsonPath("$.data.attributes.price").value(product.getPrice()));
    }

    @Test
    void getProductByIdThrowsProductNotFound() throws Exception {
        Integer productId = 1;

        ProductResponseJsonApi serviceResponse = new ProductResponseJsonApi(
                product.getId(),
                product.getName(),
                product.getPrice()
        );

        when(productService.getProductById(productId)).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(get("/api/v1/products/{productId}", productId)
                        .header("X-API-KEY", secretKey)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].status").value("404"))
                .andExpect(jsonPath("$.errors[0].title").value("Not Found"))
                .andExpect(jsonPath("$.errors[0].detail").value("Product not found"));
    }

    @Test
    void updateProductSuccess() throws Exception {
        Integer productId = 1;
        ProductRequest productUpdated = new ProductRequest(
                "Deodorant",
                BigDecimal.valueOf(12000)
        );

        ProductResponseJsonApi serviceResponse = new ProductResponseJsonApi(
                1,
                "Deodorant",
                BigDecimal.valueOf(12000)
        );

        when(productService.updateProduct(any(Integer.class), any(ProductRequest.class))).thenReturn(serviceResponse);

        mockMvc.perform(put("/api/v1/products/{productId}", productId)
                        .header("X-API-KEY", secretKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("product"))
                .andExpect(jsonPath("$.data.id").value(serviceResponse.getData().getId()))
                .andExpect(jsonPath("$.data.attributes.name").value(serviceResponse.getData().getAttributes().getName()))
                .andExpect(jsonPath("$.data.attributes.price").value(serviceResponse.getData().getAttributes().getPrice()));
    }

    @Test
    void updateProductThrowsEntityNotFoundException() throws Exception {
        Integer productId = 1;
        ProductRequest productUpdated = new ProductRequest(
                "Deodorant",
                BigDecimal.valueOf(12000)
        );

        ProductResponseJsonApi serviceResponse = new ProductResponseJsonApi(
                1,
                "Deodorant",
                BigDecimal.valueOf(12000)
        );

        when(productService.updateProduct(any(Integer.class), any(ProductRequest.class))).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(put("/api/v1/products/{productId}", productId)
                        .header("X-API-KEY", secretKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdated)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].status").value("404"))
                .andExpect(jsonPath("$.errors[0].title").value("Not Found"))
                .andExpect(jsonPath("$.errors[0].detail").value("Product not found"));
    }

    @Test
    void getProductSuccess() throws Exception {
        String basePath = "/api/v1/products";
        int page = 0;
        int size = 2;
        long totalElements = 2;
        int totalPages = 1;

        Product product1 = new Product(1, "Shampoo", BigDecimal.valueOf(15000));
        Product product2 = new Product(2, "Toothbrush", BigDecimal.valueOf(18000));
        List<Product> products = List.of(product1, product2);

        ProductListResponseJsonApi response = new ProductListResponseJsonApi(
                products,
                page,
                size,
                totalElements,
                totalPages,
                basePath
        );

        when(productService.getProducts(any(Pageable.class), eq(basePath))).thenReturn(response);

        mockMvc.perform(get("/api/v1/products")
                        .header("X-API-KEY", secretKey)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].type").value("product"))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].attributes.name").value("Shampoo"))
                .andExpect(jsonPath("$.data[0].attributes.price").value(15000))
                .andExpect(jsonPath("$.data[1].id").value("2"))
                .andExpect(jsonPath("$.data[1].attributes.name").value("Toothbrush"))
                .andExpect(jsonPath("$.meta.page").value(0))
                .andExpect(jsonPath("$.meta.size").value(2))
                .andExpect(jsonPath("$.meta.totalElements").value(2))
                .andExpect(jsonPath("$.meta.totalPages").value(1))
                .andExpect(jsonPath("$.links.self").value("/api/v1/products?page=0&size=2"));
    }

    @Test
    void getProductsReturnAnEmptyList() throws Exception {
        String basePath = "/api/v1/products";
        int page = 0;
        int size = 1;
        long totalElements = 0;
        int totalPages = 0;

        List<Product> products = new ArrayList<>();

        ProductListResponseJsonApi response = new ProductListResponseJsonApi(
                products,
                page,
                size,
                totalElements,
                totalPages,
                basePath
        );

        when(productService.getProducts(any(Pageable.class), eq(basePath))).thenReturn(response);

        mockMvc.perform(get("/api/v1/products")
                        .header("X-API-KEY", secretKey)
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.links.self").value("/api/v1/products?page=0&size=1"));
    }

    @Test
    void deleteProductSuccess() throws Exception {
        Integer productId = 1;

        when(productService.deleteProduct(productId)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/api/v1/products/{productId}", productId)
                        .header("X-API-KEY", secretKey))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deleteProductThrowsEntityNotFoundException() throws Exception {
        when(productService.deleteProduct(any(Integer.class))).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(delete("/api/v1/products/{productId}", any(Integer.class))
                        .header("X-API-KEY", secretKey))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].status").value("404"))
                .andExpect(jsonPath("$.errors[0].title").value("Not Found"))
                .andExpect(jsonPath("$.errors[0].detail").value("Product not found"));
    }

    @Test
    void deleteProductWithoutSendingTheApiKey() throws Exception {
        Integer productId = 1;

        when(productService.deleteProduct(productId)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete("/api/v1/products/{productId}", productId))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors[0].status").value("401"))
                .andExpect(jsonPath("$.errors[0].title").value("Unauthorized"))
                .andExpect(jsonPath("$.errors[0].detail").value("Invalid or missing API Key"));
    }


}
