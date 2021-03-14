package com.javaproject.storeapp.repository;

import com.javaproject.storeapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findCustomerById(int id);
}
