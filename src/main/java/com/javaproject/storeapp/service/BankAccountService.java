package com.javaproject.storeapp.service;

import com.javaproject.storeapp.entity.BankAccount;
import com.javaproject.storeapp.entity.Customer;
import com.javaproject.storeapp.exception.BankAccountNotFoundException;
import com.javaproject.storeapp.exception.DuplicateCardNumberException;
import com.javaproject.storeapp.repository.BankAccountRepository;
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
        Optional<BankAccount> existingAccountCardNumber = Optional.ofNullable(bankAccountRepository.findBankAccountByCardNumber(bankAccount.getCardNumber()));
        existingAccountCardNumber.ifPresent(e -> {
            throw new DuplicateCardNumberException(bankAccount.getCardNumber());
        });
        return bankAccountRepository.save(bankAccount);
    }

    public List<BankAccount> getBankAccountsForCustomer(int customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        return bankAccountRepository.findBankAccountsByCustomer(customerId);
    }

    public BankAccount findBankAccountById(int id) {
        Optional<BankAccount> accountOptional = Optional.ofNullable(bankAccountRepository.findBankAccountById(id));
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            throw new BankAccountNotFoundException(id);
        }
    }

    public void withdrawMoneyFromAccount(int accountId, double balance) {
        BankAccount bankAccount = findBankAccountById(accountId);
        bankAccount.setBalance(balance);
        bankAccountRepository.save(bankAccount);
    }
}
