package com.javaproject.storeapp.repository;

import com.javaproject.storeapp.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
