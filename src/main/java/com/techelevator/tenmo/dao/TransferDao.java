package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.PossibleUsersDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferNamesDTO;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    List<TransferNamesDTO> allTransfersSent(String username);

    TransferNamesDTO getTransferById(int id, String username);


   List <PossibleUsersDTO> possibleUsers(String username);

    TransferNamesDTO createTransfer(String senderName, String receiverName, double amount);

    //same as findIdByUsername in the UserDao, but included here in case a problem arises
    int findIdByUsername(String username);

}
