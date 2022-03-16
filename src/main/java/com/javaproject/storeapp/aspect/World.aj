package com.javaproject.storeapp.aspect;

public aspect World {

    pointcut greeting(): execution (* com.javaproject.storeapp.controller.ProductController.newProduct(..));

    after() returning(): greeting() {
        System.out.println("WORLD!!!");
    }
}