package com.example.spring_batch.repository;

import com.example.spring_batch.model.BankCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<BankCustomer, Integer> {

}
