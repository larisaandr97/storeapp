package com.javaproject.storeapp.exception.advice;


import com.javaproject.storeapp.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BankAccountNotFoundException.class, CustomerNotFoundException.class, CartNotFoundException.class, ProductCategoryNotFound.class, ProductNotInStock.class})
    public ResponseEntity<String> handle(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage()); //+ " at " + LocalDateTime.now());
    }

    /*@ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handle(CustomerNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body("Invalid value " + e.getFieldError().getRejectedValue()
                        + " for field " + e.getFieldError().getField()
                        + " with message: " + e.getFieldError().getDefaultMessage());

    }
}
