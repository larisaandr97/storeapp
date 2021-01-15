package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.exception.CustomerNotFoundException;
import com.javaproject.storeapp.repository.BankAccountRepository;
import com.javaproject.storeapp.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer c) {
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

}
