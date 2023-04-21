package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;

import javax.validation.Valid;


import java.math.BigDecimal;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/tenmo/accounts/")
public class AccountController {


    private AccountDao accountDao;

    public AccountController(AccountDao accountDao, JdbcUserDao jdbcUserDao, UserDao userDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "{account_id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByAccount(@PathVariable("account_id") int accountId) {
        BigDecimal balance = accountDao.getBalance(accountId);
        if (balance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
        } else {
            return balance;
        }
    }

    // Update accounts based on transfer id


//    @RequestMapping(path = "{account_id}", method = RequestMethod.PUT)
//    public void update(@Valid @RequestBody Account balance, @PathVariable int id) {
//        Account updatedBalance = accountDao.setupdatedBalance();
//        if (updatedBalance == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found.");
//        } else {
//            return updatedBalance;
//        }
//    }

//    @RequestMapping(path = "{account_id}", method = RequestMethod.PUT)
//    public void update(@Valid @RequestBody Account balance, @PathVariable int account_id) {
//        Account updatedBalance = new Account();
//        updatedBalance.setAccountId(account_id);
//        updatedBalance.setAccountId(balance.getAccountId());
//        updatedBalance.setBalance(balance.getBalance());
//        accountDao.setupdatedBalance(updatedBalance);
//    }

}