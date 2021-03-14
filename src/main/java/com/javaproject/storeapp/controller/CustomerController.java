package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.CustomerRequest;
import com.javaproject.storeapp.entity.Customer;
import com.javaproject.storeapp.mapper.BankAccountMapper;
import com.javaproject.storeapp.mapper.CustomerMapper;
import com.javaproject.storeapp.service.CustomerService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/customers")
@Validated
@Api(value = "/customers",
        tags = "Customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, BankAccountMapper bankAccountMapper, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping()
    @ApiOperation(value = "Create a Customer",
            notes = "Creates a new Customer based on the information received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The Customer was successfully created based on the received request"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public ResponseEntity<Customer> addCostumer(@Valid
                                                @RequestBody
                                                @ApiParam(name = "customer", value = "Customer details", required = true)
                                                        CustomerRequest customerRequest) {
        Customer customer = customerMapper.customerRequestToCustomer(customerRequest);

        Customer createdCustomer = customerService.createCustomer(customer);

        return ResponseEntity
                //created() will return the 201 HTTP code and set the Location header on the response, with the url to the newly created customer
                .created(URI.create("/customers/" + createdCustomer.getId()))
                //body() will populate the body of the response with the customer details
                .body(createdCustomer);

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Customer",
            notes = "Get a Customer based on the Id received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The Customer with the entered Id does not exist")
    })
    public Customer getCustomerById(@PathVariable int id) {
        return customerService.findCustomerById(id);
    }


}
