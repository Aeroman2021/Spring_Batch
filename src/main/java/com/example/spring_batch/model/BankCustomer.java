package com.example.spring_batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BANK_CUSTOMER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(name = "CUSTOMER_NAME")
    private String firstName;

    @Column(name = "CUSTOMER_SURNAME")
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true )
    @JoinColumn(name ="ACCOUNT_CUSTOMER_ID")
    private List<BankAccount> accounts = new ArrayList<>();

    @Column(name = "CUSTOMER_ADDRESS")
    private String address;

    @Column(name = "CUSTOMER_ZIP_CODE")
    private String zipCode;

    @Column(name = "CUSTOMER_NATIONAL_ID")
    private String nationalId;

    @Column(name = "CUSTOMER_DOB")
    private String dob;


}
