package com.javaproject.storeapp.service;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entity.Cart;
import com.javaproject.storeapp.entity.Product;
import com.javaproject.storeapp.entity.User;
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
    private Map<Integer, List<OrderItemRequest>> cartItems = new HashMap<>();
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    /**
     * Find cart by customer method
     *
     * @param user {@link User}
     * @return {@link Cart} Object
     */
    public Cart findCartByUser(User user) {
        return cartRepository.findCartByUser(user);
    }

    public Cart createCart(User user, double value, OrderItemRequest item) {
        Cart cart = new Cart();
        cart.setTotalAmount(value);
        cart.setUser(user);

        List<OrderItemRequest> items = new ArrayList<>();
        items.add(item);
        cartItems.put(user.getId(), items);
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

    public void addItemToCart(User user, OrderItemRequest item) {
        List<OrderItemRequest> items = cartItems.get(user.getId());
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
        cartItems.put(user.getId(), items);
    }

    public List<OrderItemRequest> deleteItemFromCart(Cart cart, int userId, int productId) {

        productService.findProductById(productId);

        List<OrderItemRequest> items = cartItems.get(userId);

        int index = IntStream.range(0, items.size())
                .filter(i -> items.get(i).getProductId() == productId)
                .findFirst().orElse(-1);

        if (index == -1) {
            throw new ProductNotInCartException(productId);
        }
        OrderItemRequest item = items.get(index);

        updateCartAmount(cart.getId(), cart.getTotalAmount() - (item.getQuantity() * item.getPrice()));
        items.remove(index);
        cartItems.put(userId, items);
        return items;
    }

    public List<OrderItemRequest> getCartContents(int userId) {
        if (getCartItems().get(userId) == null)
            throw new CartIsEmptyException(userId);
        else return cartItems.get(userId);
    }

    public Map<Integer, List<OrderItemRequest>> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Map<Integer, List<OrderItemRequest>> items) {
        this.cartItems = items;
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

    public Cart addProductToCart(User user, OrderItemRequest item) {
        Cart cart = findCartByUser(user);

        if (cart == null) {
            // if there is no existing cart for the user, we create one, also initializing the amount with the product added
            cart = createCart(user, item.getQuantity() * item.getPrice(), item);

        } else {
            addItemToCart(user, item);
            addToCartAmount(cart.getId(), item.getQuantity() * item.getPrice());
        }
        return cart;
    }
}
