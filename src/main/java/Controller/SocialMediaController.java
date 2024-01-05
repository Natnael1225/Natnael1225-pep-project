package Controller;

import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;




import java.net.http.HttpResponse;
import java.sql.SQLException;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

     AccountService accountService;
     MessageService messageService;
    public SocialMediaController(){

    }
    public SocialMediaController (AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

         app.post("/register", this::handleUserRegistration);
         app.post("/login", this::handleUserLogin);
         app.post("/messages", this::createMessage);
         app.get("/messages", this::getAllMessages);
         app.get("/messages/{message_id}", this::getMessageById);
         app.delete("/messages/{message_id}", this::deleteMessage);
         app.patch("/messages/{message_id}", this::updateMessage);
         app.get("/accounts/{account_id}/messages", this::getMessagesByAccountId);

       app.get("/message", this::exampleHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    public void handleUserRegistration(Context ctx) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse<Account> httpResponse;
        Account account = mapper.readValue(ctx.body(), Account.class);
        try{
             Account addedAccount = accountService.registerAccount(account);
             if(addedAccount!=null){
                ctx.json(mapper.writeValueAsString(addedAccount));
                ctx.status(200);
            }else{
                ctx.status(400);
            }
            } catch(SQLException se){

        }catch(Exception exception){
            System.out.println(exception.getMessage());

        }
       
    }

    private void handleUserLogin(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            Account loggedInAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
            context.json(loggedInAccount);
        } catch (Exception e) {
            context.status(401).result(e.getMessage());
        }
    }

    private void createMessage(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);
            Message createdMessage = messageService.postMessage(message);
            context.json(createdMessage);
        } catch (Exception e) {
            context.status(400).result(e.getMessage());
        }
    }

    private void getAllMessages(Context context) {
        try {
            List<Message> messages = messageService.getAllMessages();
            context.json(messages);
        } catch (Exception e) {
            context.status(500).result("Internal Server Error");
        }
    }

    private void getMessageById(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                context.json(message);
            } else {
                context.status(404).result("Message not found");
            }
        } catch (NumberFormatException e) {
            context.status(400).result("Invalid message ID format");
        } catch (Exception e) {
            context.status(500).result("Internal Server Error");
        }
    }

    private void deleteMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.deleteMessage(messageId);
            ctx.json(message);
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }

    private void updateMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message updatedMessage = ctx.bodyAsClass(Message.class);
            Message message = messageService.updateMessage(messageId, updatedMessage.getMessage_text());
            ctx.json(message);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }

    private void getMessagesByAccountId(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            ctx.json(messageService.getMessagesByAccountId(accountId));
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }

}
