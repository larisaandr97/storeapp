package com.javaproject.storeapp.dto;

import com.javaproject.storeapp.entity.Product;
import lombok.Data;

@Data
public class OrderItemRequest {
    private Product product;
    private int quantity;
    private double price;

    public OrderItemRequest(Product product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
