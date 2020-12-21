package com.javaproject.storeapp.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    private String name;
    private String description;
    private double price;

    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public Product() {
    }

    public Product(String name, String description, double price, ProductCategory productCategory, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.productCategory = productCategory;
        this.stock = stock;
    }

}
