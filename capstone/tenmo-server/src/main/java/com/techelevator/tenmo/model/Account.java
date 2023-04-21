package com.techelevator.tenmo.model;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class Account {

    private BigDecimal balance;
    private int accountId;
    private int userid;

    public Account(BigDecimal balance, int accountId, int userId) {
        this.balance = balance;
        this.accountId = accountId;
        this.userid = userId;
    }

    public Account() {
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserid(){
        return userid;
    }

    public void setUserId(int userid) {
        this.userid = userid;
    }


}
