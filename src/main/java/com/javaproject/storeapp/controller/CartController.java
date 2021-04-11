package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entity.Cart;
import com.javaproject.storeapp.entity.Order;
import com.javaproject.storeapp.entity.Product;
import com.javaproject.storeapp.entity.User;
import com.javaproject.storeapp.service.CartService;
import com.javaproject.storeapp.service.OrderService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@Api(value = "/cart",
        tags = "Cart")
public class CartController {

    private final OrderService orderService;
    private final CartService cartService;

    public CartController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add Product to Cart",
            notes = "Add the Product received in the request to the Cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Product was successfully added to the cart, returning Cart details"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })

    public String addProductToCart(@RequestParam int productId,
                                   @RequestParam int quantity, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Product product = cartService.validateProduct(productId, quantity);
        OrderItemRequest item = new OrderItemRequest(productId, quantity, product.getPrice());
        cartService.addProductToCart(user, item);
        return "redirect:/cart/";
    }

    @Transactional
    @DeleteMapping("/delete")
    @ApiOperation(value = "Delete Product from Cart",
            notes = "Deletes the Product received in the request from the Cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Product was successfully deleted from the Cart, returning Cart details"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public List<OrderItemRequest> deleteItemFromCart(@RequestParam int productId, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Cart cart = cartService.findCartByUser(user);
        return cartService.deleteItemFromCart(cart, user.getId(), productId);
    }

    @GetMapping()
    @ApiOperation(value = "Get Cart for Customer",
            notes = "Get the Cart for the Customer received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public String getCartContents(Principal principal, Model model) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        List<OrderItemRequest> items = cartService.getCartContents(user.getId());
        model.addAttribute("items", items);
        return "cart";
    }

    @PostMapping("/checkout")
    @ApiOperation(value = "Checkout Cart for Customer",
            notes = "Checkout all Products from Cart and pay with Account received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The Order was successfully created based on the received request"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public ResponseEntity<Order> checkout(@RequestParam
                                          @ApiParam(name = "accountId", value = "Account used for paying for Order", required = true)
                                                  int accountId,
                                          Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Order order = orderService.createOrder(user, cartService.getCartItems().get(user.getId()), accountId);
        return ResponseEntity
                .created(URI.create("/orders/" + order.getId()))
                .body(order);

    }
}
