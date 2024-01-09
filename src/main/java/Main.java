import static org.mockito.ArgumentMatchers.nullable;

import java.sql.SQLException;

import Controller.SocialMediaController;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import Model.Account;
import Model.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) throws JsonProcessingException, InterruptedException, IOException {
         AccountDAO accountDAO = new AccountDAO();
         MessageDAO messageDAO = new MessageDAO();
         AccountService accountService = new AccountService();
         MessageService messageService = new MessageService();
         SocialMediaController controller = new SocialMediaController();
         //MessageService messageService = new MessageService();
        Account account  = new Account();
        //account.setAccount_id(1);
        account.setPassword("Nat3");
        account.setUsername("Nat4");
        Message message = new Message();
        message.setMessage_text("Test message 234555");
        message.setPosted_by(7);
        message.setTime_posted_epoch(1234567000);
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        //String requestBody = objectMapper.writeValueAsString(account);

        String requestBody = objectMapper.writeValueAsString(message);

       
        
        // SocialMediaController controller = new SocialMediaController(accountService,  nullable(null));
        Javalin app = controller.startAPI();
        app.start(8080);


    // Create a new account object (modify as per your Account class)
    // Account newAccount = new Account("username", "password", "email@example.com");
    // ObjectMapper objectMapper = new ObjectMapper();
    // String requestBody = objectMapper.writeValueAsString(newAccount);

    // Create an HttpClient and HttpRequest for the POST request

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/register"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        // HttpRequest request = HttpRequest.newBuilder()
        //     .uri(URI.create("http://localhost:8080/login"))
        //     .header("Content-Type", "application/json")
        //     .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        //     .build();


        // HttpRequest request = HttpRequest.newBuilder()
        //     .uri(URI.create("http://localhost:8080/messages"))
        //     .header("Content-Type", "application/json")
        //     .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        //     .build();

        int messageId = 3; // Replace with an actual message ID

  
        // HttpRequest request = HttpRequest.newBuilder()
        //         .uri(URI.create("http://localhost:8080/messages/" + messageId))
        //         .GET()
        //         .build();

        // HttpRequest request = HttpRequest.newBuilder()
        // .uri(URI.create("http://localhost:8080/messages/" + messageId))
        // .DELETE()
        // .build();

        // HttpRequest request = HttpRequest.newBuilder()
        // .uri(URI.create("http://localhost:8080/messages/" + messageId))
        // .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
        // .build();


    // // Send the request and get the response
    // HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    // System.out.println("Response status code: " + response.statusCode());

    HttpClient webClient;
    webClient = HttpClient.newHttpClient();

    HttpRequest postMessageRequest = HttpRequest.newBuilder()
    .uri(URI.create("http://localhost:8080/messages"))
    .POST(HttpRequest.BodyPublishers.ofString("{"+
            "\"posted_by\":1, " +
            "\"message_text\": \"hello message\", " +
            "\"time_posted_epoch\": 1669947792}"))
    .header("Content-Type", "application/json")
    .build();

     HttpResponse response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
     int status = response.statusCode();




    // HttpRequest request = HttpRequest.newBuilder()
    //         .uri(URI.create("http://localhost:8080/messages"))
    //         .build();
    //     HttpResponse response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
    //     int status = response.statusCode();


        // HttpRequest request = HttpRequest.newBuilder()
        //         .uri(URI.create("http://localhost:8080/accounts/1/messages"))
        //         .build();
        // HttpResponse response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        // int status = response.statusCode();

        System.out.println("Status: " + status);
        System.out.println("Response body: " + response.body());
       
    }
}
