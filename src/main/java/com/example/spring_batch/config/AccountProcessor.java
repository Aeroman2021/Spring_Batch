package com.example.spring_batch.config;

import com.example.spring_batch.model.Account;
import com.example.spring_batch.model.AccountType;
import org.springframework.batch.item.ItemProcessor;

import java.util.function.Predicate;

import static com.example.spring_batch.model.AccountType.RECURRING_DEPOSIT;
import static com.example.spring_batch.model.AccountType.SAVINGS;

public class AccountProcessor implements ItemProcessor<Account, Account> {

    @Override
    public Account process(Account account) throws Exception {
        if(accountTypeIsValid(account) &&
             accountNumberIsValid(account) &&
              balanceIsValid(account) &&
                accountIsNotNull(account))
            return account;
        else
            return null;
    }

    private boolean accountTypeIsValid(Account account){
        AccountType accountType = account.getAccountType();
        return (accountType == SAVINGS ||
                accountType == RECURRING_DEPOSIT ||
                accountType == RECURRING_DEPOSIT);
    }

    private boolean balanceIsValid(Account acc) {
        return acc.getBalance() > acc.getLimit();
    }

    private boolean accountIsNotNull(Account account){
        return account != null;
    }
    private boolean accountNumberIsValid(Account account){
        String number = account.getNumber();
        return (number.startsWith("0") &&
                number.length()==10);
    }


}
