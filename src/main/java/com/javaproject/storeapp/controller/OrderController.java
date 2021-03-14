package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.entity.Order;
import com.javaproject.storeapp.service.CustomerService;
import com.javaproject.storeapp.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Api(value = "/orders",
        tags = "Orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Order",
            notes = "Get a Order based on the Id received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The Order with the entered Id does not exist!")
    })
    public Order getOrderById(@PathVariable int id) {
        return orderService.findOrderById(id);
    }

    @GetMapping("/all/{customerId}")
    @ApiOperation(value = "Get orders for a Customer",
            notes = "Retrieves all orders for a Customer.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The data was retrieved successfully"),
            @ApiResponse(code = 404, message = "Customer not found")
    })
    public List<Order> getOrdersByCustomerId(@PathVariable int customerId) {

        return orderService.getOrdersByCustomer(customerId);
    }

}
