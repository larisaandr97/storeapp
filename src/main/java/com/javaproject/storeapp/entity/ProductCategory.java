package com.javaproject.storeapp.entity;

public enum ProductCategory {
    DRAMA, COMEDIE, POLITISTE, FANTEZIE, POEZIE, BIBLIGRAFIE;

    public static boolean contains(String test) {
        for (ProductCategory c : ProductCategory.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
