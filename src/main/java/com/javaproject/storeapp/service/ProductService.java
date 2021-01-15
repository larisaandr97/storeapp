package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entities.Product;
import com.javaproject.storeapp.entities.ProductCategory;
import com.javaproject.storeapp.exception.ProductCategoryNotFound;
import com.javaproject.storeapp.exception.ProductNotFoundException;
import com.javaproject.storeapp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product p) {
        return productRepository.save(p);
    }

    public Product findProductById(int id) {
        Optional<Product> productOptional = Optional.ofNullable(productRepository.findProductById(id));
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    public List<Product> getProductsBy(String category, String name, boolean descending) {
        if (category != null) {
            String upperCaseCategory = category.toUpperCase();
            if (!ProductCategory.contains(upperCaseCategory))
                throw new ProductCategoryNotFound(category);
        }
        return productRepository.getProductsBy(category, name, descending);
    }

    public Product updateStock(int productId, int stock) {
        Product product = findProductById(productId);
        product.setStock(stock);
        return productRepository.save(product);
    }

}

