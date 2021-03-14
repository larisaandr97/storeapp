package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entity.Cart;
import com.javaproject.storeapp.entity.Customer;
import com.javaproject.storeapp.entity.Order;
import com.javaproject.storeapp.entity.Product;
import com.javaproject.storeapp.service.CartService;
import com.javaproject.storeapp.service.CustomerService;
import com.javaproject.storeapp.service.OrderService;
import com.javaproject.storeapp.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cart")
@Api(value = "/cart",
        tags = "Cart")
public class CartController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;
    private final CustomerService customerService;


    public CartController(ProductService productService, OrderService orderService, CartService cartService, CustomerService customerService) {
        this.productService = productService;
        this.orderService = orderService;
        this.cartService = cartService;
        this.customerService = customerService;
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add Product to Cart",
            notes = "Add the Product received in the request to the Cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Product was successfully added to the cart, returning Cart details"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    @Transactional
    public Cart addProductToCart(@RequestParam int customerId,
                                 @RequestParam int productId,
                                 @RequestParam int quantity) {
        Customer customer = customerService.findCustomerById(customerId);

        Product product = cartService.validateProduct(productId, quantity);

        OrderItemRequest item = new OrderItemRequest(productId, quantity, product.getPrice());

        return cartService.addProductToCart(customer, item);
    }

    @Transactional
    @DeleteMapping("/delete")
    @ApiOperation(value = "Delete Product from Cart",
            notes = "Deletes the Product received in the request from the Cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Product was successfully deleted from the Cart, returning Cart details"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public List<OrderItemRequest> deleteItemFromCart(@RequestParam int customerId,
                                                     @RequestParam int productId) {

        Customer customer = customerService.findCustomerById(customerId);

        Cart cart = cartService.findCartByCustomer(customer);

        return cartService.deleteItemFromCart(cart, customerId, productId);
    }

    @GetMapping("/{customerId}")
    @ApiOperation(value = "Get Cart for Customer",
            notes = "Get the Cart for the Customer received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public List<OrderItemRequest> getCartContents(@PathVariable int customerId) {
        customerService.findCustomerById(customerId);

        return cartService.getCartContents(customerId);
    }

    @PostMapping("/checkout/{customerId}")
    @ApiOperation(value = "Checkout Cart for Customer",
            notes = "Checkout all Products from Cart and pay with Account received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The Order was successfully created based on the received request"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public ResponseEntity<Order> checkout(@PathVariable int customerId,
                                          @RequestParam
                                          @ApiParam(name = "accountId", value = "Account used for paying for Order", required = true)
                                                  int accountId) {

        Order order = orderService.createOrder(customerId, cartService.getCartItems().get(customerId), accountId);

        return ResponseEntity
                .created(URI.create("/orders/" + order.getId()))
                .body(order);

    }
}
