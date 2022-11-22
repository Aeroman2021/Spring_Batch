package com.example.spring_batch.model;

import com.example.spring_batch.config.CustomerProcessor;
import com.example.spring_batch.repository.AccountRepository;
import com.example.spring_batch.repository.CustomerRepository;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.Optional;

@Component
public class BankAccountFieldSetMapper implements FieldSetMapper<BankAccount> {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public BankAccount mapFieldSet(FieldSet fieldSet) throws BindException {
        Optional<BankCustomer> foundCustomer = customerRepository.findById(fieldSet.readInt("customerId"));
        if(foundCustomer.isPresent()){
            return new BankAccount(fieldSet.readInt("accountId"),
                fieldSet.readString("accountNumber"),
                AccountType.of(fieldSet.readString("accountType")),
                customerRepository.findById(fieldSet.readInt("customerId")).get(),
                fieldSet.readLong("accountLimit"),
                fieldSet.readString("accountOpenDate"),
                fieldSet.readLong("accountCurrentBalance"));

        }else
            return null;
    }


}
