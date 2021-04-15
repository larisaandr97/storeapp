package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.BankAccountRequest;
import com.javaproject.storeapp.entity.BankAccount;
import com.javaproject.storeapp.entity.User;
import com.javaproject.storeapp.mapper.BankAccountMapper;
import com.javaproject.storeapp.service.BankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@Validated
@Api(value = "/accounts",
        tags = "Bank Accounts")
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountMapper bankAccountMapper;
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountMapper bankAccountMapper, BankAccountService bankAccountService) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountService = bankAccountService;
    }

    @PostMapping()
    public ResponseEntity<BankAccount> createBankAccount(
            @Valid
            @ApiParam(name = "bankAccount", value = "Bank account details", required = true)
            @RequestBody
                    BankAccountRequest bankAccountRequest,
            // @PathVariable
            //@ApiParam(name = "customerId", value = "Id of account holder", required = true)
            //       int customerId) {
            Principal principal) {
        //Customer customer = customerService.findCustomerById(customerId);
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        bankAccountRequest.setUser(user);

        BankAccount bankAccount = bankAccountMapper.bankAccountRequestToBankAccount(bankAccountRequest);
        BankAccount createdBankAccount = bankAccountService.createBankAccount(bankAccount);

        return ResponseEntity
                .created(URI.create("/bankAccount/" + createdBankAccount.getId()))
                .body(createdBankAccount);
    }

    @GetMapping()
    public ModelAndView getBankAccountsForCustomer(Principal principal) {//@PathVariable
        // @ApiParam(name = "customerId", value = "Id of account holder", required = true)
        //int customerId) {

        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        ModelAndView modelAndView = new ModelAndView("account");
        List<BankAccount> accountsFound = bankAccountService.getBankAccountsForUser(user);
        modelAndView.addObject("accounts", accountsFound);
        return modelAndView;
    }

}
