package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entities.BankAccount;
import com.javaproject.storeapp.entities.Customer;
import com.javaproject.storeapp.exception.CustomerNotFoundException;
import com.javaproject.storeapp.repository.BankAccountRepository;
import com.javaproject.storeapp.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerService customerService;

    public BankAccountService(BankAccountRepository bankAccountRepository, CustomerService customerService) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerService = customerService;
    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public List<BankAccount> getBankAccountsForCustomer(int customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        return bankAccountRepository.findBankAccountsByCustomer(customerId);
      /*  if (customerOptional.isPresent()) {
            return bankAccountRepository.findBankAccountsByCustomer(customerId);
        } else {
            throw new CustomerNotFoundException(customerId);
        }*/

    }
}
