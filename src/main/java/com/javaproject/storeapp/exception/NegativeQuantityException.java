package com.javaproject.storeapp.exception;

public class NegativeQuantityException extends RuntimeException {
    public NegativeQuantityException() {
        super("The quantity cannot be a negative number!");
    }
}
