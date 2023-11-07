package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.UsernamesDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccountController {


    private AccountDao accountDao;

    public AccountController(AccountDao accountDao){

        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public UsernamesDTO getBalance(Principal principal){

        return accountDao.getBalance(principal.getName());
    }

}
