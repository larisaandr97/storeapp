package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entities.*;
import com.javaproject.storeapp.exception.CartIsEmptyException;
import com.javaproject.storeapp.exception.CartNotFoundException;
import com.javaproject.storeapp.exception.ProductNotInStock;
import com.javaproject.storeapp.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final MainService mainService;
    private final Map<Integer, List<OrderItemRequest>> cartItems = new HashMap<>();

    public CartController(MainService mainService) {
        this.mainService = mainService;
    }

    @PostMapping("/add")
    public Cart addProductToCart(@RequestParam int customerId,
                                 @RequestParam int productId,
                                 @RequestParam int quantity) {
        Customer customer = mainService.findCustomerById(customerId);
        Product product = mainService.findProductById(productId);
        if (product.getStock() < quantity) {
            throw new ProductNotInStock(productId);
        }

        Cart cart = mainService.findCartByCustomer(customer);
        OrderItemRequest item = new OrderItemRequest(productId, quantity, product.getPrice());

        if (cart == null) {
            // if there is no existing cart for the customerId, we create one, also initializing the amount with the product added
            cart = mainService.createCart(customer, quantity * product.getPrice());
            List<OrderItemRequest> items = new ArrayList<>();
            items.add(item);
            cartItems.put(customerId, items);
        } else {
            List<OrderItemRequest> items = cartItems.get(customerId);

            int index = IntStream.range(0, items.size())
                    .filter(i -> items.get(i).getProductId() == productId)
                    .findFirst().orElse(-1);
            if (index != -1) { // item already exists in lists, only add
                OrderItemRequest old = items.get(index);
                // update quantity of item in customer's list
                items.set(index, new OrderItemRequest(old.getProductId(), old.getQuantity() + quantity, old.getPrice()));
            } else {
                items.add(item);
            }
            cartItems.put(customerId, items);
            mainService.updateCartAmount(cart.getId(), quantity * product.getPrice());
        }
        return cart;
    }

    @GetMapping("/{customerId}")
    public List<OrderItemRequest> getCartContents(@PathVariable int customerId) {
        Customer customer = mainService.findCustomerById(customerId);
        return cartItems.get(customerId);
    }

    @PostMapping("/checkout/{customerId}")
    public ResponseEntity<Order> checkout(@PathVariable int customerId, @RequestParam int accountId) {
        Customer customer = mainService.findCustomerById(customerId);
        BankAccount account = mainService.validateBankAccount(customerId, accountId);
        Cart cart = mainService.findCartByCustomer(customer);
        if (cart == null)
            throw new CartNotFoundException(customerId);
        if (cart.getTotalAmount() == 0)
            throw new CartIsEmptyException(customerId);
        Order order = mainService.createOrder(customer, cartItems.get(customerId), account);
        mainService.resetCart(cart);
        return ResponseEntity
                .created(URI.create("/orders/" + order.getId()))
                .body(order);

    }
}
