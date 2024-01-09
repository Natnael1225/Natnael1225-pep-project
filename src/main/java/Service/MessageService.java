package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;
import java.sql.SQLException;
import java.util.List;

import Model.Account;
public class MessageService {

    private MessageDAO messageDAO ;
    private AccountDAO accountDAO;
    public MessageService(){
          this.messageDAO = new MessageDAO();
          this.accountDAO = new AccountDAO();
    }

    public Message postMessage(Message message) throws Exception {
      
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            throw new Exception("Message text cannot be blank.");
        }
       
        if (message.getMessage_text().length() > 255) {
            throw new Exception("Message text cannot exceed 255 characters.");
        }
        Account account  = accountDAO.findAccountById(message.getPosted_by());
        if(account == null){
           throw new Exception();
        }
        
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.findAllMessages();
    }

    public Message getMessageById(int messageId) throws Exception {
        Message message = messageDAO.findMessageById(messageId);
        if (message == null) {
            throw new Exception("Message not found.");
        }
        return message;
    }

    public Message deleteMessage(int messageId) throws Exception {
        Message message = messageDAO.findMessageById(messageId);
        if (message == null) {
            throw new Exception("Message to delete not found.");
        }
        messageDAO.deleteMessage(messageId);
        return message;
    }

    public Message updateMessage(int messageId, String newText) throws Exception {
        if (newText == null || newText.trim().isEmpty()) {
            throw new Exception("New message text cannot be blank.");
        }
        if (newText.length() > 255) {
            throw new Exception("New message text cannot exceed 255 characters.");
        }
        Message existingMessage = messageDAO.findMessageById(messageId);
        if (existingMessage == null) {
            throw new Exception("Message to update not found.");
        }
        return messageDAO.updateMessageText(messageId, newText);
    }

    public List<Message> getMessagesByAccountId(int accountId) throws Exception {
        if (accountId <= 0) {
            throw new Exception("Invalid account ID.");
        }
        List<Message>  messages = messageDAO.findMessagesByAccountId(accountId);
        if(messages == null || messages.size() == 0){
            throw new Exception();
        }

        return messages;
    }
}
