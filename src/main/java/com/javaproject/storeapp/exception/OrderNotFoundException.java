package com.javaproject.storeapp.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(int id) {
        super("Order with Id " + id + " not found.");
    }
}
