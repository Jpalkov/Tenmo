package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.PossibleUsersDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferNamesDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class jdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    private AccountDao accountDao;

    public jdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;


    }

    @Override
    public TransferNamesDTO createTransfer(String senderUsername, String receiverUsername, double amount) {
        TransferNamesDTO newTransfer = null;

        int senderUserId = findIdByUsername(senderUsername);
        int receiverUserId = findIdByUsername(receiverUsername);



        String sql ="INSERT INTO transfer (amount, sender_id, receiver_id) " +
                "VALUES(?, ?, ?) " +
                "RETURNING transfer_id";


        try {
            int transferId = jdbcTemplate.queryForObject(sql, int.class, amount, senderUserId, receiverUserId);
            newTransfer = getTransferById(transferId, senderUsername);


            transferMoney(senderUsername, receiverUsername, amount);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return newTransfer;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        try {

            Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
            if (id != null) {
                return id;
            } else {
                return -1;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return -1;
    }


    @Override
    public List<TransferNamesDTO> allTransfersSent(String username) {
        List<TransferNamesDTO> results = new ArrayList<>();

        String sql = "SELECT transfer_id, amount, sender.username AS fromUser, receiver.username AS toUser " +
                "FROM transfer " +
                "JOIN tenmo_user sender ON transfer.sender_id = sender.user_id " +
                "JOIN tenmo_user receiver ON transfer.receiver_id = receiver.user_id " +
                "WHERE sender.username = ? OR receiver.username = ?";

        try {
            SqlRowSet queryResult = jdbcTemplate.queryForRowSet(sql, username, username);
            while (queryResult.next()){

                TransferNamesDTO current = new TransferNamesDTO();

                current.setTransferId(queryResult.getInt("transfer_id"));
                current.setTransferAmount(queryResult.getDouble("amount"));
                current.setSenderUsername(queryResult.getString("fromuser"));
                current.setReceiverUsername(queryResult.getString("touser"));

                results.add(current);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());;
        }


        return results;
    }

    @Override
    public TransferNamesDTO getTransferById(int id, String username) {

      TransferNamesDTO current = null;

        String sql = "SELECT transfer_id, amount, sender.username AS fromUser, receiver.username AS toUser " +
                "FROM transfer " +
                "JOIN tenmo_user sender ON transfer.sender_id = sender.user_id " +
                "JOIN tenmo_user receiver ON transfer.receiver_id = receiver.user_id " +
                "WHERE transfer_id = ? AND (sender.username = ? OR receiver.username = ?)";

        try {
            SqlRowSet queryResult = jdbcTemplate.queryForRowSet(sql,id,username, username);
            if (queryResult.next()){

                current = new TransferNamesDTO();

                current.setTransferId(queryResult.getInt("transfer_id"));
                current.setTransferAmount(queryResult.getDouble("amount"));
                current.setSenderUsername(queryResult.getString("fromuser"));
                current.setReceiverUsername(queryResult.getString("touser"));


            }

        } catch (Exception e){
            System.out.println(e.getMessage());;
        }


        return current;

    }

    @Override

    public List<PossibleUsersDTO> possibleUsers(String username)  {

        List<PossibleUsersDTO> userlist = new ArrayList<>();

        String sql = "SELECT username " +
                    "FROM tenmo_user " +
                "WHERE username NOT LIKE ?;";

        try {

            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            while (results.next()) {
                PossibleUsersDTO usernameCurrent = mapRowToUsernames(results);

                userlist.add(usernameCurrent);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }



        return userlist;

    }




    private Transfer mapRowToTransfer(SqlRowSet result){

        Transfer transfer = new Transfer();
        transfer.setTransfer_id(result.getInt("transfer_id"));
        transfer.setSender_id(result.getInt("sender_id"));
        transfer.setReceiver_id(result.getInt("receiver_id"));
        transfer.setAmount(result.getDouble("amount"));
        transfer.setStatus(result.getString("status"));

        return transfer;
    }

    private PossibleUsersDTO mapRowToUsernames(SqlRowSet result){

        PossibleUsersDTO username = new PossibleUsersDTO();
        username.setUsername(result.getString("username"));

        return username;
    }

    private void transferMoney(String senderUsername, String receiverUsername, double amount){


        //only one sql statement is required since both accounts are updated the same way
        String sqlUpdate = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username =?);";

        try {


            //the amount here is set as negative because inside the sql statement the amount will be subtracted
            jdbcTemplate.update(sqlUpdate, -amount, senderUsername);

            jdbcTemplate.update(sqlUpdate, amount, receiverUsername);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

}
