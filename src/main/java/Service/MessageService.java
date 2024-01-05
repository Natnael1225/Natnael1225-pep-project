package Service;

import DAO.MessageDAO;
import Model.Message;
import java.sql.SQLException;
import java.util.List;

public class MessageService {

    private MessageDAO messageDAO ;

    public MessageService(MessageDAO messageDAO){
          this.messageDAO = messageDAO;
    }

    public Message postMessage(Message message) throws Exception {
        System.out.println("Messege ---------------2 db" + message.toString());
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            throw new Exception("Message text cannot be blank.");
        }
        System.out.println("Messege --------------- 3db" + message.toString());
        if (message.getMessage_text().length() > 255) {
            throw new Exception("Message text cannot exceed 255 characters.");
        }

        System.out.println("Messege ---------------4 db" + message.toString());
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.findAllMessages();
    }

    public Message getMessageById(int messageId) throws SQLException {
        Message message = messageDAO.findMessageById(messageId);
        if (message == null) {
            throw new SQLException("Message not found.");
        }
        return message;
    }

    public Message deleteMessage(int messageId) throws SQLException {
        Message message = messageDAO.findMessageById(messageId);
        if (message == null) {
            throw new SQLException("Message to delete not found.");
        }
        messageDAO.deleteMessage(messageId);
        return message;
    }

    public Message updateMessage(int messageId, String newText) throws SQLException {
        if (newText == null || newText.trim().isEmpty()) {
            throw new SQLException("New message text cannot be blank.");
        }
        if (newText.length() > 255) {
            throw new SQLException("New message text cannot exceed 255 characters.");
        }
        Message existingMessage = messageDAO.findMessageById(messageId);
        if (existingMessage == null) {
            throw new SQLException("Message to update not found.");
        }
        return messageDAO.updateMessageText(messageId, newText);
    }

    public List<Message> getMessagesByAccountId(int accountId) throws SQLException {
        if (accountId <= 0) {
            throw new SQLException("Invalid account ID.");
        }
        return messageDAO.findMessagesByAccountId(accountId);
    }
}
