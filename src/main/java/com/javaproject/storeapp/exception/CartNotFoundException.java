package com.javaproject.storeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(int id) {
        super("Cart for customer with Id " + id + " not found.");
    }
}