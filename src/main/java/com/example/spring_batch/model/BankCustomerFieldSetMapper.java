package com.example.spring_batch.model;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class BankCustomerFieldSetMapper implements FieldSetMapper<BankCustomer> {
    @Override
    public BankCustomer mapFieldSet(FieldSet fieldSet) throws BindException {
        return new BankCustomer(fieldSet.readInt("customerId"),
                fieldSet.readRawString("customerName"),
                fieldSet.readRawString("customerSurname"),
                fieldSet.readRawString("customerAddress"),
                fieldSet.readRawString("customerZipCode"),
                fieldSet.readRawString("customerNationalId"),
                fieldSet.readRawString("customerDob"));
    }
}
