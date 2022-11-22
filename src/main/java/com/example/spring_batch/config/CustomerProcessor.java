package com.example.spring_batch.config;

import com.example.spring_batch.model.BankCustomer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<BankCustomer, BankCustomer> {

    @Override
    public BankCustomer process(BankCustomer customer) throws Exception {
        if(nationalIdIsValid(customer) && dobIsValid(customer))
            return customer;
        else
            return null;
    }

    private boolean nationalIdIsValid(BankCustomer customer){
        return customer.getCustomerNationalId().length() == 10;
    }

    private boolean dobIsValid(BankCustomer customer){
        String dob = customer.getCustomerDob();
        String year = dob.substring(6);
        int yob = Integer.parseInt(year);
        return yob < 1995;
    }


}
