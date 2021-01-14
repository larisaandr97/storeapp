package com.javaproject.storeapp.service;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entities.*;
import com.javaproject.storeapp.exception.*;
import com.javaproject.storeapp.repository.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BankAccountRepository bankAccountRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CustomerService customerService;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, BankAccountRepository bankAccountRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, CustomerService customerService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.customerService = customerService;
        this.cartService = cartService;
    }

    public Order findOrderById(int id) {
        Optional<Order> orderOptional = Optional.ofNullable(orderRepository.findOrderById(id));
        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new OrderNotFoundException(id);
        }
    }

    public List<Order> getOrdersByCustomer(Customer customer) {
        return orderRepository.findOrdersByCustomer(customer);
    }

    @Transactional
    public Order createOrder(int customerId, List<OrderItemRequest> orderItemRequests, int accountId) {

        Customer customer = customerService.findCustomerById(customerId);

        BankAccount bankAccount = validateBankAccount(customerId, accountId);

        Cart cart = cartService.findCartByCustomer(customer);
        if (cart == null)
            throw new CartNotFoundException(customerId);
        if (cart.getTotalAmount() == 0)
            throw new CartIsEmptyException(customerId);

        // Creating order object
        Order order = new Order();
        order.setCustomer(customer);
        order.setAccount(bankAccount);

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest item : orderItemRequests) {
            Product product = productRepository.findProductById(item.getProductId());
            int stock = product.getStock();
            // if there is available stock for the product, we add to list of order items
            if (stock >= item.getQuantity()) {
                product.setStock(stock - item.getQuantity());
                productRepository.save(product);
                total += product.getPrice() * item.getQuantity();
                orderItems.add(new OrderItem(item.getQuantity(), item.getPrice(), product));
            }
        }
        // check if there is enough money in the account to pay the order
        boolean result = checkBalanceForOrder(bankAccount, total);
        order.setTotalAmount(total);
        order.setDatePlaced(LocalDate.now());

        // Saving entities in database
        orderRepository.save(order);
        orderItems.forEach(item -> {
            item.setOrders(order);
            orderItemRepository.save(item);
        });

        // withdraw money from bank account
        bankAccount.setBalance(bankAccount.getBalance() - total);
        bankAccountRepository.save(bankAccount);

        cartService.resetCart(cart);
        return order;
    }

    public boolean checkBalanceForOrder(BankAccount bankAccount, double total) {
        if (bankAccount.getBalance() < total)
            throw new InsufficientFundsException(bankAccount.getId());
        else return true;
    }

    public BankAccount validateBankAccount(int customerId, int accountId) {
        BankAccount bankAccount = bankAccountRepository.findBankAccountById(accountId);
        if (bankAccount == null)
            throw new BankAccountNotFoundException(accountId);
        if (customerId != bankAccount.getCustomer().getId())
            throw new BankAccountNotBelongingToCustomer(accountId, customerId);
        return bankAccount;
    }
}
