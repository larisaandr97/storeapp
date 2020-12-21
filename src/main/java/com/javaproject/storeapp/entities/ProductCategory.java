package com.javaproject.storeapp.entities;

public enum ProductCategory {
    CLOTHES, FOOD, LAPTOPS, PHONES;

    public static boolean contains(String test) {

        for (ProductCategory c : ProductCategory.values()) {

            if (c.name().equals(test)) {

                return true;

            }

        }

        return false;

    }
}
