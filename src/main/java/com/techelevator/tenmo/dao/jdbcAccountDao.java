package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.UsernamesDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import java.security.Principal;
@Component
public class jdbcAccountDao implements  AccountDao {

    private JdbcTemplate jdbcTemplate;

    public jdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public UsernamesDTO getBalance(String username) {

        UsernamesDTO userinfo = null;

        String sql = "SELECT username, balance " +
                "FROM tenmo_user " +
                "JOIN account ON tenmo_user.user_id = account.user_id " +
                "WHERE username = ?";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
            if (result.next()) {

                userinfo = new UsernamesDTO();
                userinfo.setUsername(result.getString("username"));
                userinfo.setBalance(result.getDouble("balance"));

            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return userinfo;
    }

    @Override
    public double getBalanceByUsername(String username) {


        double newValue = 0.0;

        String sql = "SELECT balance " +
                    "FROM account " +
                     "WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?) ;";
        try{
        double result = jdbcTemplate.queryForObject(sql, double.class, username);

            newValue = result;


        } catch (DataAccessException e){
           throw new UsernameNotFoundException("getBalance is broken");
       }


        return newValue;
    }


}
