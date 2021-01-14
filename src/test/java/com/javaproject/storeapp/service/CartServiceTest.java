package com.javaproject.storeapp.service;

import com.javaproject.storeapp.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("Get Cart Contents - happy flow")
    public void getCartContentsTestHappyFlow() {

    }

    @Test
    @DisplayName("Get Cart Contents - cart is empty")
    public void getCartContentsTestCartEmpty() {

    }

    @Test
    @DisplayName("Validate Product - happy flow")
    public void validateProductTestHappyFlow() {

    }

    @Test
    @DisplayName("Validate Product - negative quantity")
    public void validateProductTestNegativeQuantity() {

    }

    @Test
    @DisplayName("Validate Product - not in stock")
    public void validateProductTestNotInStock() {

    }


}
