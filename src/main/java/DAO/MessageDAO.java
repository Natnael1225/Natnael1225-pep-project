package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message createMessage(Message message) throws SQLException {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        Connection conn = ConnectionUtil.getConnection();
           try{
              PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
              pstmt.setInt(1, message.getPosted_by());
              pstmt.setString(2, message.getMessage_text());
              pstmt.setLong(3, message.getTime_posted_epoch());
              pstmt.executeUpdate();
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    message.setMessage_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating message failed, no ID obtained.");
                }
            }catch(SQLException e){

            }
            return message;
        }
    

    public List<Message> findAllMessages() throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";
        Connection conn = ConnectionUtil.getConnection();
          try{ 
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                messages.add(extractMessage(rs));
            } 
            return messages;
            
        } catch(SQLException se){

        }
        return null;
    }

    public Message findMessageById(int messageId) throws SQLException {
          String sql = "SELECT * FROM message WHERE message_id = ?";
          Connection conn = ConnectionUtil.getConnection();
          try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, messageId);
            ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return extractMessage(rs);
                } 
                
            } catch (SQLException se){

            } 
            return null;
    }

    public void deleteMessage(int messageId) throws SQLException {
        String sql = "DELETE FROM message WHERE message_id = ?";
        Connection conn = ConnectionUtil.getConnection();
          try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, messageId);
            pstmt.executeUpdate();
        } catch(SQLException se){

        }
    }

    public Message updateMessageText(int messageId, String newText) throws SQLException {
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        Connection conn = ConnectionUtil.getConnection();
           try{
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newText);
            pstmt.setInt(2, messageId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating message failed, no rows affected.");
            }
           
        } catch(SQLException se){

        }  return findMessageById(messageId);
    }

    public List<Message> findMessagesByAccountId(int accountId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(extractMessage(rs));
                }
            }
        }
        return messages;
    }

    private Message extractMessage(ResultSet rs) throws SQLException {
        return new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
        );
    }
}
