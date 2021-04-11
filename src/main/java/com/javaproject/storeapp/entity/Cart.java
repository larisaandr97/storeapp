package com.javaproject.storeapp.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double totalAmount;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Cart() {
    }

    public Cart(int id, double totalAmount, User user) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.user = user;
    }
}
