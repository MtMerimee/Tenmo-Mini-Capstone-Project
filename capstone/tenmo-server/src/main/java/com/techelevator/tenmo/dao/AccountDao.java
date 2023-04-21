package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.JdbcUserDao;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> list();

    Account get(int id);

    BigDecimal getBalance (int id);

    void setUpdatedBalance(int accountId, BigDecimal amount);
}
