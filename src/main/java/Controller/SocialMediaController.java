package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

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

    public SocialMediaController (){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
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

    //    app.get("/message", this::exampleHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }
    public void handleUserRegistration(Context ctx) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("COntext ++++++++++ :" + ctx.toString() );
        Account account = mapper.readValue(ctx.body(), Account.class);
        System.out.println("account -------:" + account.toString() );
        try{
             Account addedAccount = accountService.registerAccount(account);
             if(addedAccount!=null){
                ctx.json(mapper.writeValueAsString(addedAccount));
                ctx.status(200);
            }else{
                ctx.status(400);
            }
        } catch(SQLException se){
            System.out.println("exception +=====" + se.toString());
        }catch(Exception exception){
            ctx.status(400);
            System.out.println("exception +=====" + exception.toString());

        }
       
    }

    private void handleUserLogin(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            Account loggedInAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
            context.json(loggedInAccount);
        } catch (Exception e) {
            context.status(401);
        }
    }

    private void createMessage(Context context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(context.body(), Message.class);
           // Message message = context.bodyAsClass(Message.class);
            System.out.println("Messege ---------------" + message.toString());
            Message createdMessage = messageService.postMessage(message);
            System.out.println("Messege ---------------4" + message.toString());
            context.json(createdMessage);
        } catch (Exception e) {
            context.status(400);
        }
    }

    private void getAllMessages(Context context) {
        try {
            List<Message> messages = messageService.getAllMessages();
            context.json(messages);
        } catch (Exception e) {
            context.status(500);
        }
    }

    private void getMessageById(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                context.json(message);
            } else {
                context.status(404);
            }
        } catch (NumberFormatException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(200);
        }
    }

    private void deleteMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.deleteMessage(messageId);
            ctx.json(message);
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(200);
        }
    }

    private void updateMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message updatedMessage = ctx.bodyAsClass(Message.class);
            Message message = messageService.updateMessage(messageId, updatedMessage.getMessage_text());
            ctx.json(message);
         } catch (Exception e) {
            ctx.status(400);
        }
    }

    private void getMessagesByAccountId(Context ctx) {
        List<Message> messages= new ArrayList<>(0);
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            messages = messageService.getMessagesByAccountId(accountId);
            ctx.status(200);
            ctx.json(messages);
        } catch (Exception e) {
            ctx.status(200);
            ctx.json(messages);
        }
    }

}
