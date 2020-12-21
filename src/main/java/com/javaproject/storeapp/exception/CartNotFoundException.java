package com.javaproject.storeapp.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(int id) {
        super("Cart for customer with Id +" + id + " not found");
    }
}