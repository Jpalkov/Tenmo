package com.techelevator.tenmo.model;

public class Account {

    private int account_id;
    private int user_id;
    private double amount;

    public Account(){

    }

    public Account(int account_id, int user_id, double amount) {
        this.account_id = account_id;
        this.user_id = user_id;
        this.amount = amount;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }



    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + account_id +
                ", user_id=" + user_id +
                ", amount=" + amount +
                '}';
    }
}
