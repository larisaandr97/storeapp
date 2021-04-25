package com.javaproject.storeapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.storeapp.dto.BankAccountRequest;
import com.javaproject.storeapp.entity.BankAccount;
import com.javaproject.storeapp.entity.User;
import com.javaproject.storeapp.mapper.BankAccountMapper;
import com.javaproject.storeapp.service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BankAccountController.class)
public class BankAccountControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BankAccountServiceImpl bankAccountService;
    //    @MockBean
//    private CustomerService customerService;
    @MockBean
    private BankAccountMapper bankAccountMapper;

    @Test
    @DisplayName("Create a new Bank Account")
    public void createBankAccountHappyFlow() throws Exception {
        User user = new User();
        user.setId(1);
        BankAccountRequest request = new BankAccountRequest("3331965465", 200, "4331256148952346", user);

        when(bankAccountService.createBankAccount(any())).thenReturn(new BankAccount(1, "3331965465", 200, "4331256148952346", user));
        // when(customerService.findCustomerById(customer.getId())).thenReturn(customer);

        mockMvc.perform(post("/accounts/" + user.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(request.getAccountNumber()))
                .andExpect(jsonPath("$.cardNumber").value(request.getCardNumber()))
                .andExpect(jsonPath("$.balance").value(request.getBalance()));
    }

}
