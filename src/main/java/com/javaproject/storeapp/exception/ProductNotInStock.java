package com.javaproject.storeapp.exception;

public class ProductNotInStock extends RuntimeException {
    public ProductNotInStock(int id) {
        super("Not enough stock available for Product with Id " + id);
    }
}
