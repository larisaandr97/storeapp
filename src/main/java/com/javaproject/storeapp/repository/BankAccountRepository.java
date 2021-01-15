package com.javaproject.storeapp.repository;

import com.javaproject.storeapp.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    BankAccount findBankAccountById(int id);

    BankAccount findBankAccountByCardNumber(String cardNumber);

    default List<BankAccount> findBankAccountsByCustomer(int customerId) {
        return this.findAll()
                .stream().filter(account -> account.getCustomer().getId() == customerId)
                .collect(Collectors.toList());
    }
}
