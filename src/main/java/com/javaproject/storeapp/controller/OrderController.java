package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.entity.Order;
import com.javaproject.storeapp.entity.User;
import com.javaproject.storeapp.service.impl.OrderServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        return orderService.findOrderById(id);
    }

    @GetMapping("/all")
    public ModelAndView getOrdersByUser(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        ModelAndView modelAndView = new ModelAndView("orders");
        List<Order> ordersFound = orderService.getOrdersByUser(user);
        modelAndView.addObject("orders", ordersFound);
        return modelAndView;
    }
}
