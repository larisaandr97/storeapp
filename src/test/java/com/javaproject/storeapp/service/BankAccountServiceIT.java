package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entities.BankAccount;
import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.repository.BankAccountRepository;
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
public class BankAccountServiceIT {

    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @AfterEach
    void tearDown() {
        bankAccountRepository.deleteAll();
    }

    @Test
    @DisplayName("Create Bank Account - happy flow")
    public void createBankAccountHappyFlow() {

        Customer customer = new Customer();
        BankAccount bankAccount = new BankAccount("3331965465", 200, "4331256148952346", customer);

        BankAccount createdAccount = bankAccountService.createBankAccount(bankAccount);

        assertNotNull(createdAccount.getId());
        assertEquals(bankAccount.getAccountNumber(), createdAccount.getAccountNumber());
        assertEquals(bankAccount.getCardNumber(), createdAccount.getCardNumber());
        assertEquals(bankAccount.getBalance(), createdAccount.getBalance());
        assertEquals(bankAccount.getCustomer(), createdAccount.getCustomer());

    }
}
