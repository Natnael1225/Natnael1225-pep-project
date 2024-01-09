package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public Account createAccount(Account account) throws SQLException {
        
        Connection connection = ConnectionUtil.getConnection();
        try { 
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            pstmt.executeUpdate();
            ResultSet pkeyResultSet = pstmt.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_author_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_author_id, account.getUsername(), account.getPassword());
            }
            System.out.println("connected Successfully");

        }catch(SQLException se){
             System.out.println(se.getMessage());
        }
        return null;
    }

    public Account findAccountByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM account WHERE username = ?";
        Connection conn = ConnectionUtil.getConnection();
        try{
             PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractAccount(rs);
            } 
        }catch(SQLException se){

        }
        return null;    
    }
    
    public Account findAccountById(int accountId) throws SQLException {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        Connection conn = ConnectionUtil.getConnection();
        try{
             PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractAccount(rs);
            } 
        }catch(SQLException se){

        }
        return null;    
    }
    


    private Account extractAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}

