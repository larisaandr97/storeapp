package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entity.Cart;
import com.javaproject.storeapp.entity.Order;
import com.javaproject.storeapp.entity.Product;
import com.javaproject.storeapp.entity.User;
import com.javaproject.storeapp.service.CartService;
import com.javaproject.storeapp.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String addProductToCart(@RequestParam int productId,
                                   @RequestParam int quantity, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Product product = cartService.validateProduct(productId, quantity);
        OrderItemRequest item = new OrderItemRequest(productId, quantity, product.getPrice());
        cartService.addProductToCart(user, item);
        return "redirect:/cart/";
    }

    @PostMapping("/update")
    public String updateCart() {
        return "cart";
    }

    @Transactional
    @PostMapping("/delete")
    public String deleteItemFromCart(@RequestParam int productId, Principal principal, Model model) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Cart cart = cartService.findCartByUser(user);
        List<OrderItemRequest> items = cartService.getCartContents(user.getId());
        model.addAttribute("items", items);
        model.addAttribute("cart", cart);
        cartService.deleteItemFromCart(cart, user.getId(), productId);
        return "cart";
    }

    @GetMapping()
    public String getCartContents(Principal principal, Model model) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Cart cart = cartService.findCartByUser(user);
        List<OrderItemRequest> items = cartService.getCartContents(user.getId());
        model.addAttribute("items", items);
        model.addAttribute("cart", cart!=null?cart:new Cart(0));
        return "cart";
    }

    @PostMapping("/checkout")
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
