package com.example.spring_batch.config;

import com.example.spring_batch.model.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        if(nationalIdIsValid(customer) && dobIsValid(customer))
            return customer;
        else
            return null;
    }

    private boolean nationalIdIsValid(Customer customer){
        return customer.getNationalId().length() == 10;
    }

    private boolean dobIsValid(Customer customer){
        String dob = customer.getDob();
        String year = dob.substring(6);
        int yob = Integer.parseInt(year);
        return yob > 1995;
    }


}
