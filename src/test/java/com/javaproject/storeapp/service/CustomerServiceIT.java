package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entity.Customer;
import com.javaproject.storeapp.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class CustomerServiceIT {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Create Customer - happy flow")
    public void createCustomerHappyFlow() {
        //arrange
        Customer customer = new Customer("Mihaila", "Mihai", "mihai@gmail.com", "Str. Viilor, nr.5");
        Customer createdCustomer = customerService.createCustomer(customer);

        assertNotNull(createdCustomer.getId());

        assertEquals(customer.getFirstName(), createdCustomer.getFirstName());
        assertEquals(customer.getLastName(), createdCustomer.getLastName());
        assertEquals(customer.getMail(), createdCustomer.getMail());
        assertEquals(customer.getAddress(), createdCustomer.getAddress());
    }


}
