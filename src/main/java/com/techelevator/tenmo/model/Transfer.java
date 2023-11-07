package com.techelevator.tenmo.model;

public class Transfer {

    private int transfer_id;
    private int sender_id;
    private int receiver_id;
    private double amount;



    private String status;


    public Transfer() {

    }

    public Transfer(int transfer_id, int sender_id, int receiver_id, double amount) {
        this.transfer_id = transfer_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.amount = amount;
        this.status = "Approved";
    }


    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;

    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transfer_id=" + transfer_id +
                ", sender_id=" + sender_id +
                ", receiver_id=" + receiver_id +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
