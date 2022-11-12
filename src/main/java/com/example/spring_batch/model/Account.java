package com.example.spring_batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "account_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "ACCOUNT_ID")
    private int accountId;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "ACCOUNT_TYPE")
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "CUSTOMER_ID")
    private Customer customerId;

    @Column(name = "LIMIT")
    private Long limit;

    @Column(name = "OPEN_DATE")
    private String openDate;

    @Column(name = "BALANCE")
    private Long balance;

}
