package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entities.Cart;
import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.entities.Product;
import com.javaproject.storeapp.exception.ProductNotInStock;
import com.javaproject.storeapp.service.MainService;
import org.springframework.web.bind.annotation.*;

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
    public void checkout(@PathVariable int customerId) {

    }
}
