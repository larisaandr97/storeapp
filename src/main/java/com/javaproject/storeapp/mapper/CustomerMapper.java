package com.javaproject.storeapp.mapper;


import com.javaproject.storeapp.dto.CustomerRequest;
import com.javaproject.storeapp.entities.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer customerRequestToCustomer(CustomerRequest customerRequest) {
        return new Customer(customerRequest.getFirstName(), customerRequest.getLastName(), customerRequest.getMail(), customerRequest.getAddress());
    }
}
