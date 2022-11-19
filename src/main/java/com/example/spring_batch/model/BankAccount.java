package com.example.spring_batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
    @Table(name = "BANK_ACCOUNT")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private int accountId;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE")
    private AccountType accountType;


    @Column(name = "ACCOUNT_LIMIT")
    private long accountLimit;

    @Column(name = "ACCOUNT_OPEN_DATE")
    private String accountOpenDate;

    @Column(name = "ACCOUNT_BALANCE")
    private Long accountCurrentBalance;

}
