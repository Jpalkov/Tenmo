package com.techelevator.dao;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.jdbcAccountDao;
import com.techelevator.tenmo.dao.jdbcTransferDao;
import com.techelevator.tenmo.model.PossibleUsersDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferNamesDTO;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class jdbcTransferDaoTests extends BaseDaoTests{

    private jdbcTransferDao sut;



    @Before
    public void setup(){

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new jdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void createTransferTest(){

       TransferNamesDTO actual = sut.createTransfer("user", "bob", 10 );

        Assert.assertEquals("user",actual.getSenderUsername() );
        Assert.assertEquals("bob",actual.getReceiverUsername() );
        Assert.assertEquals(10.0,actual.getTransferAmount(), 0.01 );

        TransferNamesDTO actual2 = sut.createTransfer("bob", "user", 100 );

        Assert.assertEquals("bob",actual2.getSenderUsername() );
        Assert.assertEquals("user",actual2.getReceiverUsername() );
        Assert.assertEquals(100.0,actual2.getTransferAmount(), 0.01 );

    }

    @Test
    public void possibleUsersTest(){

        List<PossibleUsersDTO> actual = sut.possibleUsers("user");

        //there should only be one other user displayed since you cannot send money to yourself
        Assert.assertEquals(1, actual.size());

    }

    @Test
    public void transferByIdTest(){

        sut.createTransfer("user", "bob", 10 );
        //the transfer_id should be 3001 for the previously created transfer because its the first transfer made
        TransferNamesDTO actual = sut.getTransferById(3001, "user");

        Assert.assertEquals(3001, actual.getTransferId());

        sut.createTransfer("user", "bob", 1 );
        TransferNamesDTO actual2 = sut.getTransferById(3002, "user");

        Assert.assertEquals(3002, actual2.getTransferId());

    }

    @Test
    public void listTransfersTest(){

        sut.createTransfer("user", "bob", 1);
        sut.createTransfer("user", "bob", 555);
        sut.createTransfer("bob", "user", 8.5);

        List<TransferNamesDTO> actual = sut.allTransfersSent("user");

        Assert.assertEquals(3, actual.size());

        sut.createTransfer("bob", "user", 1.5);
        sut.createTransfer("bob", "user",  5);
        sut.createTransfer("user", "bob", 1);
        sut.createTransfer("user", "bob", 1);
        sut.createTransfer("user", "bob", 1);
        sut.createTransfer("user", "bob", 1);

        List<TransferNamesDTO> actual2 = sut.allTransfersSent("bob");

        Assert.assertEquals(9, actual2.size());


    }

    @Test
    public void noTransfersTest(){

        List<TransferNamesDTO> actualEmpty = sut.allTransfersSent("user");
        //if the input username is not involved in any transfer the list size should be zero
        Assert.assertEquals(0, actualEmpty.size());
    }






}
