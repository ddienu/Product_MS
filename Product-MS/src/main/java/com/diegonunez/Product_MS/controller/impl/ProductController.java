package com.diegonunez.Product_MS.controller.impl;

import com.diegonunez.Product_MS.controller.IProductController;
import com.diegonunez.Product_MS.dto.jsonapi.ProductListResponseJsonApi;
import com.diegonunez.Product_MS.dto.jsonapi.ProductResponseJsonApi;
import com.diegonunez.Product_MS.dto.request.ProductRequest;
import com.diegonunez.Product_MS.service.IProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/products")
public class ProductController implements IProductController {

    private final IProductService productService;

    public ProductController(IProductService productService){
        this.productService = productService;
    }

    @GetMapping(path = "/{productId}")
    @Override
    public ResponseEntity<ProductResponseJsonApi> getProductById(@PathVariable Integer productId) {
        ProductResponseJsonApi serviceResponse = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(
                serviceResponse
        );
    }

    @GetMapping
    @Override
    public ResponseEntity<ProductListResponseJsonApi> getProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page, size);
        String basePath = request.getRequestURI();
        ProductListResponseJsonApi response = productService.getProducts(pageable, basePath);

        return ResponseEntity.status(HttpStatus.OK).body(
                response
        );
    }

    @PostMapping
    @Override
    public ResponseEntity<ProductResponseJsonApi> createProduct(@Valid @RequestBody ProductRequest newProduct) {
        ProductResponseJsonApi serviceResponse = productService.createNewProduct(newProduct);

        URI location = URI.create("/api/v1/products/"+ serviceResponse.getData().getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(
                "Location", location.getPath()
                ).body(
                serviceResponse
        );
    }

    @PutMapping(path = "/{productId}")
    @Override
    public ResponseEntity<ProductResponseJsonApi> updateProduct(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductRequest productUpdated) {
        ProductResponseJsonApi serviceResponse = productService.updateProduct(productId, productUpdated);

        return ResponseEntity.status(HttpStatus.OK).body(
                serviceResponse
        );
    }

    @DeleteMapping(path = "/{productId}")
    @Override
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
