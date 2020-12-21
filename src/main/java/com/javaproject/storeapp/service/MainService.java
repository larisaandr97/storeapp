package com.javaproject.storeapp.service;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.exception.BankAccountNotBelongingToCustomer;
import com.javaproject.storeapp.exception.BankAccountNotFoundException;
import com.javaproject.storeapp.exception.CustomerNotFoundException;
import com.javaproject.storeapp.entities.*;
import com.javaproject.storeapp.exception.InsufficientFundsException;
import com.javaproject.storeapp.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class MainService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;

    public MainService(ProductRepository productRepository, CustomerRepository customerRepository, OrderRepository orderRepository, BankAccountRepository bankAccountRepository, CartRepository cartRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
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

    public Order createOrder(Customer customer, List<OrderItemRequest> orderItemRequests, BankAccount bankAccount) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setAccount(bankAccount);

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest item : orderItemRequests) {
            Product product = productRepository.findProductById(item.getProductId());
            int stock = product.getStock();
            if (stock >= item.getQuantity()) {
                product.setStock(stock - item.getQuantity());
                productRepository.save(product);
                total += product.getPrice() * item.getQuantity();
                orderItems.add(new OrderItem(item.getQuantity(), item.getPrice(), product));
            }
        }
        checkBalanceForOrder(bankAccount, total);
        order.setTotalAmount(total);
        order.setDatePlaced(LocalDate.now());
        orderRepository.save(order);

        orderItems.forEach(item -> {
            item.setOrders(order);
            orderItemRepository.save(item);
        });
        bankAccount.setBalance(bankAccount.getBalance() - total);
        bankAccountRepository.save(bankAccount);
        return order;
    }

    private void checkBalanceForOrder(BankAccount bankAccount, double total) {
        if (bankAccount.getBalance() < total)
            throw new InsufficientFundsException(bankAccount.getId());
    }

    public BankAccount confirmBankAccount(int customerId, int accountId) {
        BankAccount bankAccount = bankAccountRepository.findBankAccountById(accountId);
        if (bankAccount == null)
            throw new BankAccountNotFoundException(accountId);
        if (customerId != bankAccount.getCustomer().getId())
            throw new BankAccountNotBelongingToCustomer(accountId, customerId);
        return bankAccount;
    }

    public void resetCart(Cart cart) {
        cart.setTotalAmount(0);
        cartRepository.save(cart);
    }
}

