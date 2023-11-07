package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.PossibleUsersDTO;
import com.techelevator.tenmo.model.Transfer;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.TransferNamesDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;


@RestController
public class TransferController {
    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao){
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }


   @RequestMapping(path = "/transfer", method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public TransferNamesDTO createTransfer(@RequestBody TransferDTO transferDTO, Principal principal){


        double senderBalance = accountDao.getBalanceByUsername(principal.getName());

       /*
       the if statement prevents the possibility of:
       sending 0 or negative money
       sending more money than the user has in their balance
       sending money to their own account
        */
        if (!principal.getName().equals(transferDTO.getUsername()) && senderBalance > transferDTO.getAmount() && transferDTO.getAmount() > 0) {
            return transferDao.createTransfer(principal.getName(), transferDTO.getUsername(), transferDTO.getAmount());
        }

       throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Make sure you are not sending yourself money, more money than your account balance, or an amount less than or equal to zero!");

   }
   @RequestMapping(path = "/transfers", method = RequestMethod.GET)
   public List<TransferNamesDTO> allTransfers(Principal principal){

        return transferDao.allTransfersSent(principal.getName());

   }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public TransferNamesDTO transferById(@PathVariable int id, Principal principal){

        TransferNamesDTO result = transferDao.getTransferById(id, principal.getName());

        if (result != null){

            return result;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "make sure this transfer_id is from a transfer you sent/received!");

    }


    @RequestMapping(path = "/usernames", method = RequestMethod.GET)
    public List<PossibleUsersDTO> possibleUsernames(Principal principal){

        return transferDao.possibleUsers(principal.getName());
    }





}
