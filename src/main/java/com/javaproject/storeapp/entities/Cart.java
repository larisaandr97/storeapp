package com.javaproject.storeapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double totalAmount;

    @JoinColumn(name = "customer")
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Customer customer;

    public Cart() {
    }

    public Cart(int id, double totalAmount, Customer customer) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.customer = customer;
    }
}
