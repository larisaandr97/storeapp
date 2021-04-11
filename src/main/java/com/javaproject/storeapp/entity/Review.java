package com.javaproject.storeapp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String comment;
    private int rating;
    private String author;

    @ManyToOne(cascade = CascadeType.ALL)
  //  @JoinColumn(name = "product")
    private Product product;

    public Review() {
    }

    public Review(String comment, int rating, Product product) {
        this.comment = comment;
        this.rating = rating;
        this.product = product;
    }
}
