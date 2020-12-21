package com.javaproject.storeapp.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "totalAmount")
    private double totalAmount;
    private LocalDate datePlaced;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account")
    private BankAccount account;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public Order() {
    }

    public Order(double totalAmount, LocalDate datePlaced, Customer customer) {
        this.totalAmount = totalAmount;
        this.datePlaced = datePlaced;
        this.customer = customer;
    }
}
