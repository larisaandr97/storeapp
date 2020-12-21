package com.javaproject.storeapp.dto;


import com.javaproject.storeapp.entities.Customer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.javaproject.storeapp.entities.Pattern.VISA_CREDIT_CARD;


public class BankAccountRequest {

    @NotBlank(message = "Account number cannot be null.")
    private String accountNumber;

    @NotNull
    @Min(0)
    private double balance;

    @Pattern(regexp = VISA_CREDIT_CARD, message = "Card number not valid! Only Visa accepted.")
    private String cardNumber;

    private Customer customer;

    public BankAccountRequest() {
    }

    public BankAccountRequest(String accountNumber, double balance, String cardNumber, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.customer = customer;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
