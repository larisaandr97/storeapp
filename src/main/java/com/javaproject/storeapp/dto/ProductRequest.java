package com.javaproject.storeapp.dto;

import com.javaproject.storeapp.entities.ProductCategory;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductRequest {

    @NotBlank
    @Length(min = 2, message = "Length of name must be at least 2.")
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

}
