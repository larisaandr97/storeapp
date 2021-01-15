package com.javaproject.storeapp.service;

import com.javaproject.storeapp.dto.OrderItemRequest;
import com.javaproject.storeapp.entities.*;
import com.javaproject.storeapp.exception.*;
import com.javaproject.storeapp.repository.OrderItemRepository;
import com.javaproject.storeapp.repository.OrderRepository;
import com.javaproject.storeapp.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartService cartService;
    @Mock
    private CustomerService customerService;
    @Mock
    private ProductService productService;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Find Order By Id - happy flow")
    public void findOrderByIdTestHappyFlow() {

        Order order = new Order();
        order.setId(1);
        when(orderRepository.findOrderById(order.getId()))
                .thenReturn(order);

        Order result = orderService.findOrderById(order.getId());

        assertNotNull(order.getId());
        assertEquals(order.getId(), result.getId());

    }

    @Test
    @DisplayName("Find Order By Id - order not found")
    public void findOrderByIdTestNotFound() {

        Order order = new Order();
        order.setId(1);
        when(orderRepository.findOrderById(order.getId()))
                .thenReturn(null);

        RuntimeException exception = assertThrows(OrderNotFoundException.class, () -> orderService.findOrderById(order.getId()));
        assertEquals("Order with Id " + order.getId() + " not found.", exception.getMessage());
    }


    @Test
    @DisplayName("Create order - happy flow")
    public void createOrderTestHappyFlow() {
        //arrange
        Customer customer = new Customer();
        customer.setId(1);
        BankAccount bankAccount = new BankAccount(1, "3331965465", 200, "4331256148952346", customer);
        Cart cart = new Cart(1, 100, customer);
        Product product = new Product("Lego", "disney", 100.0, ProductCategory.TOYS, 20);
        product.setId(1);
        Order orderCreated = new Order(200, LocalDate.now(), customer);
        orderCreated.setId(1);
        orderCreated.setAccount(bankAccount);
        List<OrderItemRequest> items = Collections.singletonList(new OrderItemRequest(1, 1, 100.0));
        OrderItem item = new OrderItem(1, 100, product);
        orderCreated.setOrderItems(Collections.singletonList(item));
        when(customerService.findCustomerById(anyInt())).thenReturn(customer);
        when(cartService.findCartByCustomer(customer)).thenReturn(cart);
        when(productService.findProductById(anyInt())).thenReturn(product);
        when(productService.updateStock(product.getId(), product.getStock() - item.getQuantity())).thenReturn(product);
        when(orderRepository.save(any())).thenReturn(orderCreated);
        when(orderItemRepository.save(any())).thenReturn(item);
        when(bankAccountService.findBankAccountById(anyInt())).thenReturn(bankAccount);

        //act
        Order result = orderService.createOrder(customer.getId(), items, bankAccount.getId());

        //assert
        // assertEquals(Arrays.asList(item), result.getOrderItems());
        assertEquals(cart.getTotalAmount(), result.getTotalAmount());
        assertEquals(customer, result.getCustomer());

    }

    @Test
    @DisplayName("Get Orders by Customer - happy flow")
    public void getOrdersByCustomerHappyFlow() {
        Customer customer = new Customer();
        customer.setId(1);
        when(customerService.findCustomerById(customer.getId()))
                .thenReturn(customer);
        when(orderRepository.findOrdersByCustomer(customer)).thenReturn(Collections.singletonList(new Order(100, LocalDate.now(), customer)));

        List<Order> result = orderService.getOrdersByCustomer(customer.getId());

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findOrdersByCustomer(customer);
        verify(customerService, times(1)).findCustomerById(customer.getId());
    }

    @Test
    @DisplayName("Create order - account not belonging to customer")
    public void createOrderTestAccountNotBelongingToCustomer() {
        Customer customer = new Customer();
        customer.setId(1);
        Customer anotherCustomer = new Customer();
        anotherCustomer.setId(2);
        BankAccount bankAccount = new BankAccount(1, "3331965465", 200, "4331256148952346", anotherCustomer);

        when(bankAccountService.findBankAccountById(anyInt())).thenReturn(bankAccount);
        BankAccountNotBelongingToCustomer exception = assertThrows(BankAccountNotBelongingToCustomer.class, () -> orderService.validateBankAccount(customer.getId(), bankAccount.getId()));
        assertEquals("Bank account with Id " + bankAccount.getId() + " does not belong to customer with Id " + customer.getId() + ".", exception.getMessage());
    }

    @Test
    @DisplayName("Create order - cart not found")
    public void createOrderTestCartNotFound() {
        Customer customer = new Customer();
        customer.setId(1);
        BankAccount bankAccount = new BankAccount(1, "3331965465", 200, "4331256148952346", customer);

        when(bankAccountService.findBankAccountById(bankAccount.getId())).thenReturn(bankAccount);
        when(cartService.findCartByCustomer(any())).thenReturn(null);

        CartNotFoundException exception = assertThrows(CartNotFoundException.class, () -> orderService.createOrder(customer.getId(), new ArrayList<>(), bankAccount.getId()));
        assertEquals("Cart for customer with Id " + customer.getId() + " not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Create order - cart is empty")
    public void createOrderTestCartIsEmpty() {
        Customer customer = new Customer();
        customer.setId(1);
        BankAccount bankAccount = new BankAccount(1, "3331965465", 200, "4331256148952346", customer);
        Cart cart = new Cart();
        cart.setTotalAmount(0);

        when(bankAccountService.findBankAccountById(bankAccount.getId())).thenReturn(bankAccount);
        when(cartService.findCartByCustomer(any())).thenReturn(cart);

        CartIsEmptyException exception = assertThrows(CartIsEmptyException.class, () -> orderService.createOrder(customer.getId(), new ArrayList<>(), bankAccount.getId()));
        assertEquals("Cart for customer with Id " + customer.getId() + " is empty! You must add some items before making an order.", exception.getMessage());
    }

    @Test
    @DisplayName("Check balance of bank account for order - happy flow")
    public void checkBalanceForOrderTestHappyFlow() {
        BankAccount bankAccount = new BankAccount("3331965465", 200, "4331256148952346", null);
        double totalAmount = 100;
        boolean result = orderService.checkBalanceForOrder(bankAccount, totalAmount);
        assert (result);
    }

    @Test
    @DisplayName("Check balance of bank account for order - insufficient funds")
    public void checkBalanceForOrderTestInsufficientFunds() {
        BankAccount bankAccount = new BankAccount(1, "3331965465", 200, "4331256148952346", null);
        double totalAmount = 350;

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> orderService.checkBalanceForOrder(bankAccount, totalAmount));
        assertEquals("Insufficient funds in Bank Account with Id " + bankAccount.getId() + ".", exception.getMessage());

    }
}
