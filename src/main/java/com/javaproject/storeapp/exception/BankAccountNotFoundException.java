package com.javaproject.storeapp.exception;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(int id) {
        super("Bank account with Id " + id + " not found.");
    }

}
