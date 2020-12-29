package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entities.Product;
import com.javaproject.storeapp.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product p) {
        return productRepository.save(p);
    }

    public Product findProductById(int id) {
        return productRepository.findProductById(id);
    }

    public List<Product> getProductsBy(String category, String name, boolean descending) {
        return productRepository.getProductsBy(category, name, descending);
    }


}

