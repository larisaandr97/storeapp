package com.javaproject.storeapp.exception.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    //@ExceptionHandler({BankAccountNotFoundException.class, CustomerNotFoundException.class, CartNotFoundException.class, ProductCategoryNotFound.class, ProductNotInStock.class, InsufficientFundsException.class, BankAccountNotBelongingToCustomer.class})
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage()); //+ " at " + LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body("Invalid value " + Objects.requireNonNull(e.getFieldError()).getRejectedValue()
                        + " for field " + e.getFieldError().getField()
                        + " with message: " + e.getFieldError().getDefaultMessage());

    }
}
