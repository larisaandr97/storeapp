package com.javaproject.storeapp.repository;

import com.javaproject.storeapp.entity.Cart;
import com.javaproject.storeapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findCartById(int id);

    Cart findCartByCustomer(Customer customer);
}
