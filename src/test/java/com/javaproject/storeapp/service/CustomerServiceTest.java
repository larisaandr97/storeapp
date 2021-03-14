package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entity.Customer;
import com.javaproject.storeapp.exception.CustomerNotFoundException;
import com.javaproject.storeapp.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("Add a new Customer")
    public void createCustomerTest() {

        //arrange
        Customer customer = new Customer("Mihaila", "Mihai", "mihai@gmail.com", "Str. Viilor, nr.5");
        Customer savedCustomer = new Customer(1, "Mihaila", "Mihai", "mihai@gmail.com", "Str. Viilor, nr.5");
        when(customerRepository.save(customer)).thenReturn(savedCustomer);

        //act
        Customer result = customerService.createCustomer(customer);

        //assert
        assertEquals(customer.getFirstName(), result.getFirstName());
        assertEquals(customer.getLastName(), result.getLastName());
        assertEquals(customer.getMail(), result.getMail());
        assertEquals(customer.getAddress(), result.getAddress());
        verify(customerRepository, Mockito.times((1))).save(customer);
    }

    @Test
    @DisplayName("Find Customer By Id - happy flow")
    public void findCustomerByIdTestHappyFlow() {

        Customer customer = new Customer();
        customer.setId(1);
        when(customerRepository.findCustomerById(customer.getId()))
                .thenReturn(customer);

        Customer result = customerService.findCustomerById(customer.getId());

        assertNotNull(result.getId());
        assertEquals(customer.getId(), result.getId());

    }

    @Test
    @DisplayName("Find Customer By Id - customer not found")
    public void findCustomerByIdTestNotFound() {

        Customer customer = new Customer();
        customer.setId(1);
        when(customerRepository.findCustomerById(customer.getId()))
                .thenReturn(null);

        RuntimeException exception = assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomerById(customer.getId()));
        assertEquals("Customer with Id " + customer.getId() + " not found.", exception.getMessage());

    }


}
