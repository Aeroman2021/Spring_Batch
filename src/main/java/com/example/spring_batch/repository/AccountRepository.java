package com.example.spring_batch.repository;

import com.example.spring_batch.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount,Integer> {
}
