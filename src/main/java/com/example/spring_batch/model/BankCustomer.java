package com.example.spring_batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BANK_CUSTOMER")
public class BankCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private int customerId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_SURNAME")
    private String customerSurname;

    @Column(name = "CUSTOMER_ADDRESS")
    private String customerAddress;

    @Column(name = "CUSTOMER_ZIP_CODE")
    private String customerZipCode;

    @Column(name = "CUSTOMER_NATIONAL_ID")
    private String customerNationalId;

    @Column(name = "CUSTOMER_BIRTH_DATE")
    private String customerDob;

}
