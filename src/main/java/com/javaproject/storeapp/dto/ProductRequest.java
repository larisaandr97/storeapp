package com.javaproject.storeapp.dto;

import com.javaproject.storeapp.entities.ProductCategory;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotBlank
    @Length(min=2, message = "Length of name must be at least 2.")
    private String name;

    @NotNull
    @Length(max = 100, message = "Description must not exceed 100 characters.")
    private String description;

    @NotNull
    @Min(0)
    private double price;

    @NotNull
    @Min(0)
    private int stock;

    @NotNull
    private ProductCategory productCategory;

    public ProductRequest(String name, String description, double price, int stock, ProductCategory productCategory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.productCategory = productCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }


}
