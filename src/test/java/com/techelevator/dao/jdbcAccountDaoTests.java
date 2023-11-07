package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.jdbcAccountDao;
import com.techelevator.tenmo.model.UsernamesDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class jdbcAccountDaoTests extends BaseDaoTests{

    private jdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new jdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getBalanceTest(){

        UsernamesDTO actual = sut.getBalance("user");

        Assert.assertEquals(1000.0, actual.getBalance(), 0.01);

        UsernamesDTO actual2 = sut.getBalance("bob");

        Assert.assertEquals(123.4, actual2.getBalance(), 0.01);


    }

    @Test
    public void getBalanceByUsernameTest(){

        double actual = sut.getBalanceByUsername("user");

        Assert.assertEquals(1000.0, actual, 0.01);

        double actual2 = sut.getBalanceByUsername("bob");

        Assert.assertEquals(123.4, actual2, 0.01);

    }
}
