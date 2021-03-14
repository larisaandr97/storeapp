package com.javaproject.storeapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bankAccount")
@Data
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "accountNumber")
    private String accountNumber;
    private double balance;
    @Column(name = "cardNumber")
    private String cardNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer")
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;

    public BankAccount() {
    }

    public BankAccount(String accountNumber, double balance, String cardNumber, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.customer = customer;
    }

    public BankAccount(int id, String accountNumber, double balance, String cardNumber, Customer customer) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.customer = customer;
    }
}
