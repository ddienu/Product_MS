package com.diegonunez.Product_MS.service.impl;

import com.diegonunez.Product_MS.dto.jsonapi.ProductListResponseJsonApi;
import com.diegonunez.Product_MS.dto.jsonapi.ProductResponseJsonApi;
import com.diegonunez.Product_MS.dto.request.ProductRequest;
import com.diegonunez.Product_MS.entity.Product;
import com.diegonunez.Product_MS.repository.IProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductByIdReturnProduct(){
        Product mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setName("Deodorant");
        mockProduct.setPrice(new BigDecimal("65000.00"));

        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        ProductResponseJsonApi productFounded = productService.getProductById(1);

        Assertions.assertNotNull(productFounded);
        Assertions.assertEquals(mockProduct.getName(), productFounded.getData().getAttributes().getName());
        Assertions.assertEquals(mockProduct.getPrice(), productFounded.getData().getAttributes().getPrice());
        verify(productRepository).findById(1);
    }

    @Test
    void getProductByIdThrowsEntityNotFoundException() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> productService.getProductById(1)
        );

        Assertions.assertEquals("Product not found", thrown.getMessage());
        verify(productRepository).findById(1);
    }

    @Test
    void getProductsCorrectly(){

        List<Product> productList = List.of(
                new Product(1, "Shampoo", BigDecimal.valueOf(19000)),
                new Product(2, "Deodorant", BigDecimal.valueOf(8900)),
                new Product(3, "Soap", BigDecimal.valueOf(19000))
        );

        String baseUrl = "/api/v1/product";

        Page<Product> productPage = new PageImpl<>(productList);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        ProductListResponseJsonApi result = productService.getProducts(PageRequest.of(0, 10), baseUrl);

        Assertions.assertEquals(productList.size(), result.getData().size());
        Assertions.assertEquals(productList.get(0).getId().toString(), result.getData().get(0).getId());
        Assertions.assertEquals(productList.get(1).getName(), result.getData().get(1).getAttributes().getName());
        Assertions.assertEquals(productList.get(2).getPrice(), result.getData().get(2).getAttributes().getPrice());
    }

    @Test
    void getProductsReturnsEmptyList(){
        List<Product> productList = List.of();

        String baseUrl = "/api/v1/product";

        Page<Product> productPage = new PageImpl<>(productList);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        ProductListResponseJsonApi result = productService.getProducts(PageRequest.of(0, 1), baseUrl);

        Assertions.assertEquals(new ArrayList<>(), result.getData());
    }

    @Test
    void createNewProductSuccess(){
        ProductRequest newProduct = new ProductRequest();
        newProduct.setName("Shampoo");
        newProduct.setPrice(BigDecimal.valueOf(18000));

        Product productToAdd = new Product();
        productToAdd.setId(1);
        productToAdd.setName(newProduct.getName());
        productToAdd.setPrice(newProduct.getPrice());

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(1);
            return p;
        });

        ProductResponseJsonApi result = productService.createNewProduct(newProduct);

        Assertions.assertEquals(productToAdd.getId().toString(), result.getData().getId());
        Assertions.assertEquals(productToAdd.getName(), result.getData().getAttributes().getName());
        Assertions.assertEquals(productToAdd.getPrice(), result.getData().getAttributes().getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProductSuccess(){
        Integer productId = 1;

        ProductRequest productUpdated = new ProductRequest(
                "Shampoo",
                BigDecimal.valueOf(15000)
        );

        Product productFounded = new Product(
                1,
                "Deodorant",
                BigDecimal.valueOf(18000)
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(productFounded));

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(1);
            return p;
        });

        ProductResponseJsonApi result = productService.updateProduct(productId, productUpdated);

        Assertions.assertEquals(productUpdated.getName(), result.getData().getAttributes().getName());
        Assertions.assertEquals(productUpdated.getPrice(), result.getData().getAttributes().getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProcessNoEffectBecauseValuesToUpdateAreSameThanTheActualOnes(){
        Integer productId = 1;

        ProductRequest productUpdated = new ProductRequest(
                "Shampoo",
                BigDecimal.valueOf(15000)
        );

        Product productFounded = new Product(
                1,
                "Shampoo",
                BigDecimal.valueOf(15000)
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(productFounded));

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(1);
            return p;
        });

        ProductResponseJsonApi result = productService.updateProduct(productId, productUpdated);

        Assertions.assertEquals(productUpdated.getName(), result.getData().getAttributes().getName());
        Assertions.assertEquals(productUpdated.getPrice(), result.getData().getAttributes().getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProductThrowsEntityNotFoundException(){
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> productService.updateProduct(1, any(ProductRequest.class))
        );

        Assertions.assertEquals("Product not found", thrown.getMessage());
        verify(productRepository).findById(1);
    }

    @Test
    void deleteProductSuccess(){
        Product productFounded = new Product(
                1,
                "Shampoo",
                BigDecimal.valueOf(18000)
        );
        when(productRepository.findById(1)).thenReturn(Optional.of(productFounded));

        Boolean result = productService.deleteProduct(1);

        Assertions.assertEquals(Boolean.TRUE, result);
        verify(productRepository).findById(1);
    }

    @Test
    void deleteProductThrowsEntityNotFoundException(){
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> productService.deleteProduct(1)
        );

        Assertions.assertEquals("Product not found", thrown.getMessage());
        verify(productRepository).findById(1);
    }




}
