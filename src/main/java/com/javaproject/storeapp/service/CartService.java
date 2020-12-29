package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entities.Cart;
import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart findCartByCustomer(Customer customer) {
        return cartRepository.findCartByCustomer(customer);
    }

    public Cart createCart(Customer customer, double value) {
        Cart cart = new Cart();
        cart.setTotalAmount(value);
        cart.setCustomer(customer);
        return cartRepository.save(cart);
    }

    public void updateCartAmount(int cartId, double value) {
        Cart cart = cartRepository.findCartById(cartId);
        cart.setTotalAmount(cart.getTotalAmount() + value);
        cartRepository.save(cart);
    }

    public void resetCart(Cart cart) {
        cart.setTotalAmount(0);
        cartRepository.save(cart);
    }
}
