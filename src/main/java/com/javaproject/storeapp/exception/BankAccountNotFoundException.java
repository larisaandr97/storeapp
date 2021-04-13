package com.javaproject.storeapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(int id) {
        super("Bank account with Id " + id + " not found.");
    }

}
