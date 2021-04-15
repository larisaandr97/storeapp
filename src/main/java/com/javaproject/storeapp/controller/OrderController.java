package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.entity.Order;
import com.javaproject.storeapp.entity.User;
import com.javaproject.storeapp.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Api(value = "/orders",
        tags = "Orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        return orderService.findOrderById(id);
    }

    @GetMapping("/all")
    public List<Order> getOrdersByUser(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return orderService.getOrdersByUser(user);
    }
}
