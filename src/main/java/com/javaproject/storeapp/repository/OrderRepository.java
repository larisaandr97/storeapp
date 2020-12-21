package com.javaproject.storeapp.repository;

import com.javaproject.storeapp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById(int id);
    default List<Order> findOrdersByCustomerId(int customerId){
        return this.findAll().stream().filter(product -> product.getCustomer().getId() == customerId).collect(Collectors.toList());
    }
}
