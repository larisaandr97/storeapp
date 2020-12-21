package com.javaproject.storeapp.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(int id) {
        super("Insufficient funds in bank account with id " + id + ".");
    }
}
