package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entities.*;
import com.javaproject.storeapp.exception.CartIsEmptyException;
import com.javaproject.storeapp.exception.CartNotFoundException;
import com.javaproject.storeapp.exception.NegativeQuantityException;
import com.javaproject.storeapp.exception.ProductNotInStock;
import com.javaproject.storeapp.service.CartService;
import com.javaproject.storeapp.service.CustomerService;
import com.javaproject.storeapp.service.ProductService;
import com.javaproject.storeapp.service.OrderService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.*;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/cart")
@Api(value = "/cart",
        tags = "Cart")
public class CartController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;
    private final CustomerService customerService;
    private final Map<Integer, List<OrderItemRequest>> cartItems = new HashMap<>();

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
            @ApiResponse(code = 201, message = "The Product was successfully added to the cart"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    @Transactional
    public Cart addProductToCart(@RequestParam int customerId,
                                 @RequestParam int productId,
                                 @RequestParam int quantity) {
        Customer customer = customerService.findCustomerById(customerId);
        Product product = productService.findProductById(productId);

        if (quantity <= 0)
            throw new NegativeQuantityException();
        if (product.getStock() < quantity) {
            throw new ProductNotInStock(productId);
        }

        Cart cart = cartService.findCartByCustomer(customer);
        OrderItemRequest item = new OrderItemRequest(productId, quantity, product.getPrice());

        if (cart == null) {
            // if there is no existing cart for the customerId, we create one, also initializing the amount with the product added
            cart = cartService.createCart(customer, quantity * product.getPrice());

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
            cartService.updateCartAmount(cart.getId(), quantity * product.getPrice());
        }
        return cart;
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "Delete Product from Cart",
            notes = "Deletes the Product received in the request from the Cart")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The Product was successfully deleted from the cart"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public List<OrderItemRequest> deleteItemFromCart(@RequestParam int customerId,
                                                     @RequestParam int productId) {
        List<OrderItemRequest> items = cartItems.get(customerId);
        int index = IntStream.range(0, items.size())
                .filter(i -> items.get(i).getProductId() == productId)
                .findFirst().orElse(-1);
        items.remove(index);
        cartItems.put(customerId, items);
        return items;
    }

    @GetMapping("/{customerId}")
    @ApiOperation(value = "Get Cart for Customer",
            notes = "Get the Cart for the Customer received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public List<OrderItemRequest> getCartContents(@PathVariable int customerId) {
        customerService.findCustomerById(customerId);

        if (cartItems.get(customerId) == null)
            throw new CartIsEmptyException(customerId);
        else return cartItems.get(customerId);
    }

    @PostMapping("/checkout/{customerId}")
    @ApiOperation(value = "Checkout Cart for Customer",
            notes = "Checkout all Products from Cart and pay with account received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The Order was successfully created based on the received request"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public ResponseEntity<Order> checkout(@PathVariable int customerId,
                                          @RequestParam
                                          @ApiParam(name = "account", value = "Account used for paying for order", required = true)
                                                  int accountId) {
        Customer customer = customerService.findCustomerById(customerId);

        BankAccount account = orderService.validateBankAccount(customerId, accountId);

        Cart cart = cartService.findCartByCustomer(customer);
        if (cart == null)
            throw new CartNotFoundException(customerId);
        if (cart.getTotalAmount() == 0)
            throw new CartIsEmptyException(customerId);

        Order order = orderService.createOrder(customer, cartItems.get(customerId), account);

        cartService.resetCart(cart);
        return ResponseEntity
                .created(URI.create("/orders/" + order.getId()))
                .body(order);

    }
}
