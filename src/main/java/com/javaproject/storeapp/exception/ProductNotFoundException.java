package com.javaproject.storeapp.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(int id) {
        super("Product with Id " + id + " not found.");
    }
}
