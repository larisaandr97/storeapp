package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.BankAccountRequest;
import com.javaproject.storeapp.dto.CustomerRequest;
import com.javaproject.storeapp.mapper.BankAccountMapper;
import com.javaproject.storeapp.mapper.CustomerMapper;
import com.javaproject.storeapp.entities.BankAccount;
import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Validated
public class CustomerController {

    private final MainService mainService;
    private final BankAccountMapper bankAccountMapper;
    private final CustomerMapper customerMapper;

    public CustomerController(MainService mainService, BankAccountMapper bankAccountMapper, CustomerMapper customerMapper) {
        this.mainService = mainService;
        this.bankAccountMapper = bankAccountMapper;
        this.customerMapper = customerMapper;
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> addCostumer(@Valid
                                                @RequestBody CustomerRequest customerRequest) {
        Customer customer = customerMapper.customerRequestToCustomer(customerRequest);

        Customer createdCustomer = mainService.addCustomer(customer);
        return ResponseEntity
                //created() will return the 201 HTTP code and set the Location header on the response, with the url to the newly created customer
                .created(URI.create("/customers/" + createdCustomer.getId()))
                //body() will populate the body of the response with the customer details
                .body(createdCustomer);

    }

    @GetMapping("customers/{id}")
    public Customer getCustomerById(@PathVariable int id) {
        return mainService.findCustomerById(id);
    }

    /* Bank Account */
    @PostMapping("/accounts/{customerId}")
    public ResponseEntity<BankAccount> createBankAccount(
            @Valid
            @RequestBody BankAccountRequest bankAccountRequest, @PathVariable int customerId) {
        Customer customer = getCustomerById(customerId);
        bankAccountRequest.setCustomer(customer);
        BankAccount bankAccount = bankAccountMapper.bankAccountRequestToBankAccount(bankAccountRequest);
        BankAccount createdBankAccount = mainService.createBankAccount(bankAccount);
        return ResponseEntity
                .created(URI.create("/bankAccount/" + createdBankAccount.getId()))
                .body(createdBankAccount);
    }

    @GetMapping("/accounts/{customerId}")
    public List<BankAccount> getBankAccountsForCustomer(@PathVariable int customerId){
        Customer customer = getCustomerById(customerId);
        return mainService.getBankAccountsForCustomer(customerId);
    }

}
