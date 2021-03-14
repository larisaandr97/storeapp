package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.BankAccountRequest;
import com.javaproject.storeapp.entity.BankAccount;
import com.javaproject.storeapp.entity.Customer;
import com.javaproject.storeapp.mapper.BankAccountMapper;
import com.javaproject.storeapp.service.BankAccountService;
import com.javaproject.storeapp.service.CustomerService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Validated
@Api(value = "/accounts",
        tags = "Bank Accounts")
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountMapper bankAccountMapper;
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;

    public BankAccountController(BankAccountMapper bankAccountMapper, BankAccountService bankAccountService, CustomerService customerService) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountService = bankAccountService;
        this.customerService = customerService;
    }

    @ApiOperation(value = "Create a Bank Account",
            notes = "Creates a new Bank Account based on the information received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The Bank Account was successfully created based on the received request"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    @PostMapping("/{customerId}")
    public ResponseEntity<BankAccount> createBankAccount(
            @Valid
            @ApiParam(name = "bankAccount", value = "Bank account details", required = true)
            @RequestBody
                    BankAccountRequest bankAccountRequest,
            @PathVariable
            @ApiParam(name = "customerId", value = "Id of account holder", required = true)
                    int customerId) {
        Customer customer = customerService.findCustomerById(customerId);

        bankAccountRequest.setCustomer(customer);

        BankAccount bankAccount = bankAccountMapper.bankAccountRequestToBankAccount(bankAccountRequest);
        BankAccount createdBankAccount = bankAccountService.createBankAccount(bankAccount);

        return ResponseEntity
                .created(URI.create("/bankAccount/" + createdBankAccount.getId()))
                .body(createdBankAccount);
    }

    @GetMapping("/{customerId}")
    @ApiOperation(value = "Get Bank Accounts for Customer",
            notes = "Get all Bank Accounts for a Customer based on the Id received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The Customer with the entered Id does not exist!")
    })
    public List<BankAccount> getBankAccountsForCustomer(@PathVariable
                                                        @ApiParam(name = "customerId", value = "Id of account holder", required = true)
                                                                int customerId) {
        return bankAccountService.getBankAccountsForCustomer(customerId);
    }

}
