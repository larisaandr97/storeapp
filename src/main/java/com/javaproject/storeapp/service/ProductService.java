package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entity.Product;
import com.javaproject.storeapp.entity.ProductCategory;
import com.javaproject.storeapp.exception.ProductCategoryNotFound;
import com.javaproject.storeapp.exception.ResourceNotFoundException;
import com.javaproject.storeapp.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            throw new ResourceNotFoundException("Product with Id " + id + " not found.");
        }
    }

    public List<Product> getProductsBy(String category, String name, boolean descending) {
        if (category != null) {
            String upperCaseCategory = category.toUpperCase();
            if (!ProductCategory.contains(upperCaseCategory))
                throw new ProductCategoryNotFound(category);
        }
        Pageable sortedByPrice =
                PageRequest.of(0, 6, descending ? Sort.by("price").descending() : Sort.by("price").ascending());
        return productRepository.getProductsBy(category, name, descending, sortedByPrice);
    }

    public Product updateStock(int productId, int stock) {
        Product product = findProductById(productId);
        product.setStock(stock);
        return productRepository.save(product);
    }

}

