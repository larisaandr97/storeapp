package com.javaproject.storeapp.repository;

import com.javaproject.storeapp.entities.Cart;
import com.javaproject.storeapp.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findCartById(int id);

    Cart findCartByCustomer(Customer customer);

   /* default Cart findCartByCustomerId(int customerId) {
        Optional<Cart> cartOptional = this.findAll().stream().filter(cart -> {
            int id = cart.getCustomer().getId();
            return id == customerId;
        }).findFirst();
        return cartOptional.orElse(null);
    }*/
}
