package com.javaproject.storeapp.repository;


import com.javaproject.storeapp.exception.ProductCategoryNotFound;
import com.javaproject.storeapp.entities.Product;
import com.javaproject.storeapp.entities.ProductCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findProductById(int id);

    default List<Product> getProductsBy(String category, String name, boolean descending) {

        if (category != null) {
            String upperCaseCategory = category.toUpperCase();
            if (!ProductCategory.contains(upperCaseCategory))
                throw new ProductCategoryNotFound(category);
        }
        return this.findAll(descending ? Sort.by("price").descending() : Sort.by("price").ascending()).stream()
                .filter(product -> {
                    if (category != null) {//we filter by category only if the category was sent in the request
                        if (name != null) { //we filter by name only if the name was sent in the request
                            return ProductCategory.valueOf(category.toUpperCase()).equals(product.getProductCategory()) && product.getName().toLowerCase().contains(name.toLowerCase());
                        } else {
                            return ProductCategory.valueOf(category.toUpperCase()).equals(product.getProductCategory());
                        }
                    } else if (name != null) { //we filter by name only if the name was sent in the request
                        return product.getName().toLowerCase().contains(name.toLowerCase());
                    } else {//no filters are sent in the request, all products should be returned
                        return true;
                    }
                })
                .collect(Collectors.toList());

    }
}