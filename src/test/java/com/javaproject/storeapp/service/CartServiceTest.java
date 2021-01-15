package com.javaproject.storeapp.service;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entities.Product;
import com.javaproject.storeapp.entities.ProductCategory;
import com.javaproject.storeapp.exception.CartIsEmptyException;
import com.javaproject.storeapp.exception.NegativeQuantityException;
import com.javaproject.storeapp.exception.ProductNotInStockException;
import com.javaproject.storeapp.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("Get Cart Contents - happy flow")
    public void getCartContentsTestHappyFlow() {
        Map<Integer, List<OrderItemRequest>> cartItems = new HashMap<>();
        cartItems.put(1, Collections.singletonList(new OrderItemRequest(1, 1, 50)));
        int customerId = 1;
        cartService.setCartItems(cartItems);

        List<OrderItemRequest> result = cartService.getCartContents(customerId);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Get Cart Contents - cart is empty")
    public void getCartContentsTestCartEmpty() {
        Map<Integer, List<OrderItemRequest>> cartItems = new HashMap<>();
        cartItems.put(1, null);
        int customerId = 1;
        cartService.setCartItems(cartItems);

        CartIsEmptyException exception = assertThrows(CartIsEmptyException.class, () -> cartService.getCartContents(customerId));

        assertEquals("Cart for customer with Id " + customerId + " is empty! You must add some items before making an order.", exception.getMessage());
    }

    @Test
    @DisplayName("Validate Product - happy flow")
    public void validateProductTestHappyFlow() {
        Product product = new Product("Sapiens", "self-development book", 50, ProductCategory.BOOKS, 10);
        product.setId(1);
        when(productService.findProductById(anyInt())).thenReturn(product);
        int quantity = 2;

        Product result = cartService.validateProduct(product.getId(), quantity);

        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
    }

    @Test
    @DisplayName("Validate Product - negative quantity")
    public void validateProductTestNegativeQuantity() {
        Product product = new Product("Sapiens", "self-development book", 50, ProductCategory.BOOKS, 10);
        product.setId(1);
        when(productService.findProductById(anyInt())).thenReturn(product);
        int quantity = -1;

        NegativeQuantityException exception = assertThrows(NegativeQuantityException.class, () -> cartService.validateProduct(product.getId(), quantity));
        assertEquals("The quantity cannot be zero or a negative number!", exception.getMessage());
    }

    @Test
    @DisplayName("Validate Product - not in stock")
    public void validateProductTestNotInStock() {
        Product product = new Product("Sapiens", "self-development book", 50, ProductCategory.BOOKS, 2);
        product.setId(1);
        when(productService.findProductById(anyInt())).thenReturn(product);
        int quantity = 4;

        ProductNotInStockException exception = assertThrows(ProductNotInStockException.class, () -> cartService.validateProduct(product.getId(), quantity));
        assertEquals("Not enough stock available for Product with Id " + product.getId() + ".", exception.getMessage());
    }

}
