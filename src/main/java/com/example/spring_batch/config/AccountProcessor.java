package com.example.spring_batch.config;

import com.example.spring_batch.model.BankAccount;
import com.example.spring_batch.model.AccountType;
import org.springframework.batch.item.ItemProcessor;

import static com.example.spring_batch.model.AccountType.*;

public class AccountProcessor implements ItemProcessor<BankAccount, BankAccount> {

    @Override
    public BankAccount process(BankAccount account) throws Exception {
        if(accountTypeIsValid(account) &&
             accountNumberIsValid(account) &&
              balanceIsValid(account) &&
                accountIsNotNull(account)) {
            System.out.println("data valid");
            return account;
        }else {
            System.out.println("data is invalid");
            return null;
        }
    }

    private boolean accountTypeIsValid(BankAccount account){
        AccountType accountType = account.getAccountType();
        return (accountType == SAVINGS ||
                accountType == FIXED_DEPOSIT ||
                accountType == RECURRING_DEPOSIT);
    }

    private boolean balanceIsValid(BankAccount acc) {
        return acc.getAccountCurrentBalance() > acc.getAccountLimit();
    }

    private boolean accountIsNotNull(BankAccount account){
        return account != null;
    }
    private boolean accountNumberIsValid(BankAccount account){
        String number = account.getAccountNumber();
        return (number.startsWith("1") &&
                number.length()==9);
    }


}
