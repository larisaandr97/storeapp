package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.entities.Order;
import com.javaproject.storeapp.service.MainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final MainService mainService;

    public OrderController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        return mainService.findOrderById(id);
    }

    @GetMapping("/all/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable int customerId) {
        return mainService.getOrdersByCustomerId(customerId);
    }

}
