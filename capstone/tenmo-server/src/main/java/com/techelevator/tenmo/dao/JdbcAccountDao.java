package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> list() {

        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE user_id = ?;";


        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }

        return accounts;
    }


    @Override
    public BigDecimal getBalance (int id) {
        BigDecimal balance = BigDecimal.valueOf(0.0);
        String sql = "SELECT balance " +
                "FROM account " +
                "WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            balance = results.getBigDecimal("balance");

        }
        return balance;
    }



    @Override
    public void setUpdatedBalance(int accountId, BigDecimal amount) {
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        jdbcTemplate.update(sql, amount, accountId);
    }



    @Override
    public Account get(int id) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            Account account = new Account();
            account.setAccountId(results.getInt("account_id"));
            account.setUserId(results.getInt("user_id"));
            account.setBalance(results.getBigDecimal("balance"));
            return account;
        } else {
            return null;
        }
    }










  /*  @Override
    public Account getCurrentBalance(int id) {
        String sql = "SELECT balance FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE user_id = ?;";
        return null;
    }*/



    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;

}

}

