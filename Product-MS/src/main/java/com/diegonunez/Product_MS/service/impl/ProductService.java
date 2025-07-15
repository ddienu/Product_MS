package com.diegonunez.Product_MS.service.impl;

import com.diegonunez.Product_MS.dto.jsonapi.ProductListResponseJsonApi;
import com.diegonunez.Product_MS.dto.jsonapi.ProductResponseJsonApi;
import com.diegonunez.Product_MS.dto.request.ProductRequest;
import com.diegonunez.Product_MS.entity.Product;
import com.diegonunez.Product_MS.mapper.ProductJsonApiMapper;
import com.diegonunez.Product_MS.repository.IProductRepository;
import com.diegonunez.Product_MS.service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseJsonApi getProductById(Integer productId) {
        Product productFounded = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product not found")
        );

        return ProductJsonApiMapper.toJsonApi(productFounded);
    }

    @Override
    public ProductListResponseJsonApi getProducts(Pageable pageable, String basePath) {

        Page<Product> page = productRepository.findAll(pageable);

        return new ProductListResponseJsonApi(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                basePath
        );
    }

    @Transactional
    @Override
    public ProductResponseJsonApi createNewProduct(ProductRequest newProduct) {
        Product productToAdd = new Product();
        productToAdd.setName(newProduct.getName());
        productToAdd.setPrice(newProduct.getPrice());

        productRepository.save(productToAdd);

        return ProductJsonApiMapper.toJsonApi(productToAdd);
    }

    @Transactional
    @Override
    public ProductResponseJsonApi updateProduct(Integer productId, ProductRequest productUpdated) {
        Product productFounded = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product not found")
        );

        if(!productFounded.getName().equals(productUpdated.getName())){
            productFounded.setName(productUpdated.getName());
        }

        if(!productFounded.getPrice().equals(productUpdated.getPrice())){
            productFounded.setPrice(productUpdated.getPrice());
        }

        productRepository.save(productFounded);

        return ProductJsonApiMapper.toJsonApi(productFounded);
    }

    @Override
    public Boolean deleteProduct(Integer productId) {
        Product productFounded = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product not found")
        );

        productRepository.delete(productFounded);

        return true;
    }
}
