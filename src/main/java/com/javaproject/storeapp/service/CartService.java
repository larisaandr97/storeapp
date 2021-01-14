package com.javaproject.storeapp.service;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entities.Cart;
import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.entities.Product;
import com.javaproject.storeapp.exception.CartIsEmptyException;
import com.javaproject.storeapp.exception.NegativeQuantityException;
import com.javaproject.storeapp.exception.ProductNotInCartException;
import com.javaproject.storeapp.exception.ProductNotInStockException;
import com.javaproject.storeapp.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final Map<Integer, List<OrderItemRequest>> cartItems = new HashMap<>();
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Cart findCartByCustomer(Customer customer) {
        return cartRepository.findCartByCustomer(customer);
    }

    public Cart createCart(Customer customer, double value, OrderItemRequest item) {
        Cart cart = new Cart();
        cart.setTotalAmount(value);
        cart.setCustomer(customer);

        List<OrderItemRequest> items = new ArrayList<>();
        items.add(item);
        cartItems.put(customer.getId(), items);
        return cartRepository.save(cart);
    }

    public void updateCartAmount(int cartId, double value) {
        Cart cart = cartRepository.findCartById(cartId);
        cart.setTotalAmount(value);
        cartRepository.save(cart);
    }

    public void addToCartAmount(int cartId, double value) {
        Cart cart = cartRepository.findCartById(cartId);
        cart.setTotalAmount(cart.getTotalAmount() + value);
        cartRepository.save(cart);
    }

    public void resetCart(Cart cart) {
        cart.setTotalAmount(0);
        cartRepository.save(cart);
    }

    public void addItemToCart(Customer customer, OrderItemRequest item) {
        List<OrderItemRequest> items = cartItems.get(customer.getId());
        int index = IntStream.range(0, items.size())
                .filter(i -> items.get(i).getProductId() == item.getProductId())
                .findFirst().orElse(-1);

        if (index != -1) { // item already exists in lists, only add
            OrderItemRequest old = items.get(index);

            // update quantity of item in customer's list
            items.set(index, new OrderItemRequest(old.getProductId(), old.getQuantity() + item.getQuantity(), old.getPrice()));

        } else {
            items.add(item);
        }
        cartItems.put(customer.getId(), items);
    }

    public List<OrderItemRequest> deleteItemFromCart(Cart cart, int customerId, int productId) {

        productService.findProductById(productId);

        List<OrderItemRequest> items = cartItems.get(customerId);

        int index = IntStream.range(0, items.size())
                .filter(i -> items.get(i).getProductId() == productId)
                .findFirst().orElse(-1);

        if (index == -1) {
            throw new ProductNotInCartException(productId);
        }
        OrderItemRequest item = items.get(index);

        updateCartAmount(cart.getId(), cart.getTotalAmount() - (item.getQuantity() * item.getPrice()));
        items.remove(index);
        cartItems.put(customerId, items);
        return items;
    }

    public List<OrderItemRequest> getCartContents(int customerId) {
        if (cartItems.get(customerId) == null)
            throw new CartIsEmptyException(customerId);
        else return cartItems.get(customerId);
    }

    public Map<Integer, List<OrderItemRequest>> getCartItems() {
        return cartItems;
    }

    public Product validateProduct(int productId, int quantity) {
        Product product = productService.findProductById(productId);

        if (quantity <= 0)
            throw new NegativeQuantityException();
        if (product.getStock() < quantity) {
            throw new ProductNotInStockException(productId);
        }
        return product;
    }

    public Cart addProductToCart(Customer customer, OrderItemRequest item) {
        Cart cart = findCartByCustomer(customer);

        if (cart == null) {
            // if there is no existing cart for the customerId, we create one, also initializing the amount with the product added
            cart = createCart(customer, item.getQuantity() * item.getPrice(), item);

        } else {
            addItemToCart(customer, item);
            addToCartAmount(cart.getId(), item.getQuantity() * item.getPrice());
        }
        return cart;
    }
}
