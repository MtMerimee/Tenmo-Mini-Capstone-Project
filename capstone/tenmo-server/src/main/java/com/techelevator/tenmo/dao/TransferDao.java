package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listTransferHistory(int senderId, int receiverId);

    Transfer getTransferById(int transferId);

    List<Transfer> searchForTransferByUserId(int id);

    List<Transfer> listPendingTransactions(int senderId, int receiverId);

    boolean approveTransfer(int transferId);

    void rejectTransfer(int transferId);

    Transfer sendTransfer(Transfer transfer);

    Transfer requestTransfer(Transfer transfer);

//    Boolean createTransfer(int transferTypeId, int transferStatusID, int accountFrom, int accountTo, BigDecimal amount);
}
