package com.example.spring_batch.config;

import com.example.spring_batch.model.Account;
import com.example.spring_batch.model.AccountType;
import org.springframework.batch.item.ItemProcessor;

public class AccountProcessor implements ItemProcessor<Account, Account> {

    @Override
    public Account process(Account account) throws Exception {
        return account;
    }
}
