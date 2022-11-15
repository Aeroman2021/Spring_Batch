package com.example.spring_batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "BANK_ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @Id
    @Column(name = "ACCOUNT_ID")
    private int accountId;

    @Column(name = "NUMBER")
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE")
    private AccountType accountType;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private BankCustomer customer;

    @Column(name = "ACCOUNT_LIMIT")
    private Long limit;

    @Column(name = "ACCOUNT_OPEN_DATE")
    private String openDate;

    @Column(name = "ACCOUNT_BALANCE")
    private Long balance;

}
