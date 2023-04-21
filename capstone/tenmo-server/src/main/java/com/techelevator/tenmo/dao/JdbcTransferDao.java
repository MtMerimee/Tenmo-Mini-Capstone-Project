package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // Get Transfer History
//    @Override
//    public List<Transfer> listTransferHistory() {
//        List<Transfer> transferHistory = new ArrayList<>();
//        String sql = " SELECT  t.transfer_id,  t.amount,  u1.username AS sender_username, u2.username as receiver_username\n" +
//                " FROM transfer t  \n" +
//                " JOIN account a1 ON t.account_from = a1.account_id \n" +
//                " JOIN tenmo_user u1 ON a1.user_id = u1.user_id  \n" +
//                " JOIN account a2 ON t.account_to = a2.account_id  \n" +
//                " JOIN tenmo_user u2 ON a2.user_id = u2.user_id";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
//        while (results.next()) {
//            Transfer transfer = mapRowToTransfer(results);
//            transferHistory.add(transfer);
//        }
//
//        return transferHistory;
//    }
    @Override
    public List<Transfer> listTransferHistory(int senderId, int receiverId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount\n" +
                "from transfer\n" +
                "where account_from = ? or account_to = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, senderId, receiverId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    // Get Transfer By Id
//    @Override
//    public Transfer getTransferById ( int transferId){
//        String sql = "select transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount\n" +
//                "from transfer\n" +
//                "where transfer_id = ?;";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
//        if (results.next()) {
//            return mapRowToTransfer(results);
//        } else {
//            return null;
//        }
//    }

    @Override
    public Transfer getTransferById ( int transferId){
        String sql = "select * \n" +
                "from transfer\n" +
                "where transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        } else {
            return null;
        }
    }

    // Get Pending Transactions
    @Override
    public List<Transfer> listPendingTransactions(int senderId, int receiverId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer.transfer_id as transfer_id, \n" +
                "           transfer_status.transfer_status_desc as status, \n" +
                "           transfer.account_from as account_from, \n" +
                "           transfer.account_to as account_to, \n" +
                "           transfer.amount as amount\n" +
                "from transfer\n" +
                "join transfer_status on transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "where transfer.transfer_status_id = 1 and (transfer.account_from = ? or transfer.account_to = ?)\n";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, senderId, receiverId);
        while (results.next()) {
            Transfer transfer = mapRowToPendingTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }
    // Send Transfer
    @Override
    public Transfer sendTransfer(Transfer transfer) {
        int transferTypeId = 2; // Send
        int transferStatusId = 1; // Pending
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?)";
       jdbcTemplate.update(sql, transferTypeId, transferStatusId,
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
       return transfer;
    }


    // request transfer
    @Override
    public Transfer requestTransfer(Transfer transfer) {
        int transferTypeId = 1; // Request
        int transferStatusId = 1; // Pending
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, transferTypeId, transferStatusId,
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        if (rowsAffected == 1) {
            return transfer;
        } else {
            return null;
        }
    }

    // Approve a Transfer
    @Override
    public boolean approveTransfer(int transferId) {
        // First, retrieve the transfer to approve
        Transfer transfer = getTransferById(transferId);

        // Check that the transfer exists and is currently in a pending state
        if (transfer == null || transfer.getTransferStatusId() != 1) {
            return false;
        }

        // Update the transfer status to Approved
        String sql = "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = ?";
        int numRowsUpdated = jdbcTemplate.update(sql, transferId);

        // Check that the update was successful
        if (numRowsUpdated == 1) {
            return true;
        } else {
            return false;
        }
    }

    // Reject a transfer
    @Override
    public void rejectTransfer(int transferId) {
        String sql = "UPDATE transfer SET transfer_status_id = 3 WHERE transfer_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, transferId);
        if (rowsAffected != 1) {
            throw new RuntimeException("Failed to update transfer");
        }
    }

    // set transfer type

    @Override
    public List<Transfer> searchForTransferByUserId ( int id){
        return null;
    }

//    @Override
//    public Boolean createTransfer(int transferTypeId, int transferStatusID, int accountFrom, int accountTo, BigDecimal amount) {
//        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                "VALUES (?, ?, ?, ?, ?) " +
//                "RETURNING transfer_id;";
//        Integer newTransferId;
//        newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusID, accountFrom, accountTo, amount);
//        if (newTransferId == null) {
//            return false;
//        } else {
//            return true;
//        }
//    }

//    @Override
//    public Boolean createTransfer(int transferTypeId, int transferStatusID, int accountFrom, int accountTo, BigDecimal amount) {
//        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                "VALUES (?, ?, ?, ?, ?) " +
//                "RETURNING transfer_id;";
//        Integer newTransferId;
//
////        logger.info("Inserting transfer record with values transferTypeId={}, transferStatusID={}, accountFrom={}, accountTo={}, amount={}", transferTypeId, transferStatusID, accountFrom, accountTo, amount);
//        newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusID, accountFrom, accountTo, amount);
////        logger.info("Inserted new transfer record with ID {}", newTransferId);
//
//        if (newTransferId == null) {
////            logger.error("Failed to create transfer record with values transferTypeId={}, transferStatusID={}, accountFrom={}, accountTo={}, amount={}", transferTypeId, transferStatusID, accountFrom, accountTo, amount);
//            return false;
//        } else {
////            logger.info("Successfully created transfer record with ID {} and values transferTypeId={}, transferStatusID={}, accountFrom={}, accountTo={}, amount={}", newTransferId, transferTypeId, transferStatusID, accountFrom, accountTo, amount);
//            return true;
//        }
//    }

//    @Override
//    public Boolean createTransfer(int transferTypeId, int transferStatusID, int accountFrom, int accountTo, BigDecimal amount) {
//        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                "VALUES (?, ?, ?, ?, ?) " +
//                "RETURNING transfer_id;";
//        Integer newTransferId;
//        newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusID, accountFrom, accountTo, amount);
//
//        if (newTransferId == null) {
//            return false;
//        } else {
//            return true;
//        }
//    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        Integer accountTo = rs.getInt("account_to"); // Use Integer instead of int for null check
        if (accountTo == null || accountTo == 0) { // Check for null as well
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account To is null or 0");
        }
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }

    private Transfer mapRowToPendingTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferStatus(rs.getString("status"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
    }


