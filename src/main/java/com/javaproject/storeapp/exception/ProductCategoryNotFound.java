package com.javaproject.storeapp.exception;

public class ProductCategoryNotFound extends RuntimeException {
    public ProductCategoryNotFound(String name) {
        super("Product category " + name + " not found");
    }
}
