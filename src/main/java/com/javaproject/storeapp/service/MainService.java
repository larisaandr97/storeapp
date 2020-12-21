package com.javaproject.storeapp.service;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.exception.CustomerNotFoundException;
import com.javaproject.storeapp.entities.*;
import com.javaproject.storeapp.repository.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MainService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CartRepository cartRepository;

    public MainService(ProductRepository productRepository, CustomerRepository customerRepository, OrderRepository orderRepository, BankAccountRepository bankAccountRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.cartRepository = cartRepository;
    }

    /* Product */
    public Product addProduct(Product p) {
        return productRepository.save(p);
    }

    public Product findProductById(int id) {
        return productRepository.findProductById(id);
    }

    public List<Product> getProductsBy(String category, String name, boolean descending) {
        return productRepository.getProductsBy(category, name, descending);
    }

    /* Customer */
    public Customer addCustomer(Customer c) {
        return customerRepository.save(c);
    }

    public Customer findCustomerById(int id) {
        Optional<Customer> customerOptional = Optional.ofNullable(customerRepository.findCustomerById(id));
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        } else {
            throw new CustomerNotFoundException(id);
        }

    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public List<BankAccount> getBankAccountsForCustomer(int customerId) {
        return bankAccountRepository.findBankAccountsByCustomer(customerId);
    }

    /* Order */
    public Order findOrderById(int id) {
        return orderRepository.findOrderById(id);
    }


    /* Cart */
    public Cart findCartByCustomer(Customer customer) {
        return cartRepository.findCartByCustomer(customer);
    }

    public Cart createCart(Customer customer, double value) {
        Cart cart = new Cart();
        cart.setTotalAmount(value);
        cart.setCustomer(customer);
        return cartRepository.save(cart);
    }

    public void updateCartAmount(int cartId, double value) {
        Cart cart = cartRepository.findCartById(cartId);
        cart.setTotalAmount(cart.getTotalAmount() + value);
        cartRepository.save(cart);
    }

    public List<Order> getOrdersByCustomerId(int customerId) {
        Customer customer = findCustomerById(customerId);
        return orderRepository.findOrdersByCustomerId(customerId);
    }
}

