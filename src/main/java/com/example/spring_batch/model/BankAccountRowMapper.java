package com.example.spring_batch.model;

import com.example.spring_batch.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class BankAccountRowMapper implements RowMapper<BankAccount> {

    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public BankAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
        BankAccount account = new BankAccount();
        account.setAccountId(rs.getInt("ACCOUNT_ID"));
        account.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
        account.setAccountType(AccountType.of(rs.getNString("accountType")));
        account.setCustomerId( customerRepository.findById(rs.getInt("customerId")).get());
        account.setAccountLimit(rs.getLong("ACCOUNT_LIMIT"));
        account.setAccountOpenDate(rs.getString("ACCOUNT_OPEN_DATE"));
        account.setAccountCurrentBalance(rs.getLong("ACCOUNT_BALANCE"));

        return account;
    }
}
