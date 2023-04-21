package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {


        private int transferId;
        private int transferStatusId;
        private int transferTypeId;
        private int accountFrom;
        private int accountTo;
        private BigDecimal amount;

        private String transferStatus;
        private int transferType;
//        private String account;
//        private int accountId;


    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferStatusId() {
        return transferStatusId;

    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }


    public BigDecimal getAmount() {
            return amount;
        }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTransferId() {
            return transferId;
        }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }
    //
//    public int getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(int accountId) {
//        this.accountId = accountId;
//    }
//
//    public String getAccount() {
//        return account;
//    }
//
//    public void setAccount(String account) {
//        this.account = account;
//    }

    }

