package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entities.BankAccount;
import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.exception.DuplicateCardNumberException;
import com.javaproject.storeapp.repository.BankAccountRepository;
import com.javaproject.storeapp.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @Test
    @DisplayName("Add a new BankAccount - happy flow")
    public void createBankAccountTestHappyFlow() {

        //arrange
        Customer customer = new Customer();
        customer.setId(1);
        BankAccount bankAccount = new BankAccount("3331965465", 200, "4331256148952346", customer);
        BankAccount savedBankAccount = new BankAccount(1, "3331965465", 200, "4331256148952346", customer);
        when(bankAccountRepository.save(bankAccount)).thenReturn(savedBankAccount);

        //act
        BankAccount result = bankAccountService.createBankAccount(bankAccount);

        //assert
        assertEquals(bankAccount.getCardNumber(), result.getCardNumber());
        assertEquals(bankAccount.getAccountNumber(), result.getAccountNumber());
        assertEquals(bankAccount.getBalance(), result.getBalance());
        verify(bankAccountRepository, Mockito.times((1))).save(bankAccount);
    }

    @Test
    @DisplayName("Add a new BankAccount - bank account with same card number already exists")
    public void createBankAccountTestDuplicateException() {

        //arrange
        Customer customer = new Customer();
        customer.setId(1);
        BankAccount bankAccount = new BankAccount("3331965465", 200, "4331256148952346", customer);
        BankAccount savedBankAccount = new BankAccount(1, "3331965465", 200, "4331256148952346", customer);
        when(bankAccountRepository.findBankAccountByCardNumber(any())).thenReturn(savedBankAccount);

        //act
        DuplicateCardNumberException exception = assertThrows(DuplicateCardNumberException.class, () -> bankAccountService.createBankAccount(bankAccount));

        //assert
        assertEquals("A bank account with the card number " + bankAccount.getCardNumber() + " already exists.", exception.getMessage());
        verify(bankAccountRepository, times(1)).findBankAccountByCardNumber(any());
        verify(bankAccountRepository, times(0)).save(any());

    }

    @Test
    @DisplayName("Get all BankAccounts for a Customer")
    public void getBankAccountForCustomerHappyFlow() {
        //arrange
        Customer customer = new Customer();
        customer.setId(1);

        BankAccount bankAccount1 = new BankAccount(1, "3331965465", 200, "4331256148952346", customer);
        BankAccount bankAccount2 = new BankAccount(2, "4441965465", 340, "4229256148952346", customer);

        customer.setBankAccounts(Arrays.asList(bankAccount1, bankAccount2));

        when(customerService.findCustomerById(customer.getId()))
                .thenReturn(customer);
        when(bankAccountRepository.findBankAccountsByCustomer(customer.getId()))
                .thenReturn(Arrays.asList(bankAccount1, bankAccount2));

        //act
        List<BankAccount> result = bankAccountService.getBankAccountsForCustomer(customer.getId());

        //assert
        assertEquals(customer.getBankAccounts(), result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getBalance(), 200);

        verify(customerService, times(1)).findCustomerById(customer.getId());
        verify(bankAccountRepository, times(1)).findBankAccountsByCustomer(customer.getId());
    }

    /*@Test
    @DisplayName("Get Bank Accounts for Customer - case when customer does not exist")
    public void getBankAccountForCustomerSadFlow() {
        //arrange
        Customer customer = new Customer();
        customer.setId(1);

        when(customerRepository.findCustomerById(customer.getId()))
                .thenReturn(null);

        //act and assert
        assertThrows(CustomerNotFoundException.class, () -> bankAccountService.getBankAccountsForCustomer(customer.getId()));

    }*/

}
