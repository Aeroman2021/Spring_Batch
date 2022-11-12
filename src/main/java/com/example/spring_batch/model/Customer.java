package com.example.spring_batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "customer_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private int id;

    @Column(name = "NAME")
    private String firstName;

    @Column(name = "SURNAME")
    private String lastName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "NATIONAL_ID")
    private String nationalId;

    @Column(name = "DOB")
    private String dob;


}
