package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.entities.Order;
import com.javaproject.storeapp.service.CustomerService;
import com.javaproject.storeapp.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        return orderService.findOrderById(id);
    }

    @GetMapping("/all/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable int customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        return orderService.getOrdersByCustomer(customer);
    }

}
