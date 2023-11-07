package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.UsernamesDTO;

import java.security.Principal;

public interface AccountDao {

    UsernamesDTO getBalance(String username);

    double getBalanceByUsername(String username);






}
