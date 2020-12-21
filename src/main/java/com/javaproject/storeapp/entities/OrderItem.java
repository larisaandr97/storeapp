package com.javaproject.storeapp.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "orderItem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int quantity;
    private int price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orders")
    private Order orders;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product")
    private Product product;

    public OrderItem() {
    }

    public OrderItem(int quantity, int price, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }
}
