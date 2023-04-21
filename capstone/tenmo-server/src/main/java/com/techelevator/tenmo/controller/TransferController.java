package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/tenmo/transfers")
public class TransferController {

    private final TransferDao transferDao;
    private final AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    // See Transfer History
    @RequestMapping(path = "/{account_id}/history", method = RequestMethod.GET)
    public List<Transfer> listTransferHistory(@PathVariable("account_id") int accountId) {
        return transferDao.listTransferHistory(accountId, accountId);
    }

    // Get TransferById
    @RequestMapping(path = "/id/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId) {
        Transfer transfer = transferDao.getTransferById(transferId);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer Not Found");
        } else {
            return transfer;
        }
    }

    // Get Pending Transactions
    @RequestMapping(path = "/{accountId}/pending", method = RequestMethod.GET)
    public List<Transfer> listPendingTransactions(@PathVariable("accountId") int accountId) {
        return transferDao.listPendingTransactions(accountId, accountId);
    }

    // send transfer
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public Transfer sendTransfer(@RequestBody Transfer transfer) {
        // Check that the sender has enough funds to make the transfer
        BigDecimal balance = accountDao.getBalance(transfer.getAccountFrom());
        if (balance.compareTo(transfer.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        // Set the transfer type and status
        transfer.setTransferType(2);
        transfer.setTransferStatusId(1);

        // Create the transfer and update the account balances
        transferDao.sendTransfer(transfer);

        return transfer;
    }

    // request transfer
    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public Transfer requestTransfer(@RequestBody Transfer transfer) {
        // Check that the sender has enough funds to make the transfer
        BigDecimal balance = accountDao.getBalance(transfer.getAccountFrom());
        if (balance.compareTo(transfer.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        // Set the transfer type and status
        transfer.setTransferType(1);
        transfer.setTransferStatusId(1);

        // Create the transfer
        transferDao.requestTransfer(transfer);
        return transfer;
    }

    // Approve Transfer
    @RequestMapping(path = "/id/{transferId}/approve", method = RequestMethod.PUT)
    public ResponseEntity<Object> approveTransfer(@PathVariable int transferId) {
        try {
            // Retrieve the transfer to approve
            Transfer transfer = transferDao.getTransferById(transferId);

            // Get the account_from and account_to IDs
            int accountFromId = transfer.getAccountFrom();
            int accountToId = transfer.getAccountTo();

            // Update the account balances
            BigDecimal amount = transfer.getAmount();
            accountDao.setUpdatedBalance(accountFromId, amount.negate());
            accountDao.setUpdatedBalance(accountToId, amount);

            // Approve the transfer
            boolean result = transferDao.approveTransfer(transferId);
            if (result) {
                return new ResponseEntity<>("Transfer approved successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to approve transfer", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Reject Transfer
    @RequestMapping(path = "/id/{transferId}/reject", method = RequestMethod.PUT)
    public ResponseEntity<Object> rejectTransfer(@PathVariable int transferId) {
        try {
            transferDao.rejectTransfer(transferId);
            return new ResponseEntity<>("Transfer rejected successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to reject transfer", HttpStatus.BAD_REQUEST);
        }
    }

//        @RequestMapping(path = "/create", method = RequestMethod.POST)
//        public void createTransfer(@Valid @RequestBody Transfer transfer) {
//            if (transfer == null) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found");
//            }
//            Account fromAccount = accountDao.get(transfer.getAccountFrom());
//            Account toAccount = accountDao.get(transfer.getAccountTo());
//            if (fromAccount == null || toAccount == null) {
//                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts");
//            }
//            if (fromAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
//            }
//            fromAccount.setBalance(fromAccount.getBalance().subtract(transfer.getAmount()));
//            toAccount.setBalance(toAccount.getBalance().add(transfer.getAmount()));
//            accountDao.setupdatedBalance(fromAccount);
//            accountDao.setupdatedBalance(toAccount);
//        }


}