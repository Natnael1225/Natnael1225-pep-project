package Service;

import DAO.AccountDAO;
import Model.Account;
import java.sql.SQLException;

public class AccountService {

    private AccountDAO accountDAO;
    public AccountService (){
        this.accountDAO = new AccountDAO();
    }
    public Account registerAccount(Account account) throws Exception {
        if (account.getUsername() == null || account.getUsername().isEmpty() ||
            account.getPassword() == null || account.getPassword().length() < 4) {
            throw new Exception("Invalid registration details");
        }
         if (accountDAO.findAccountByUsername(account.getUsername()) != null) {
             throw new Exception("Username already exists");
         }
        Account insertedAccount = accountDAO.createAccount(account);
        System.out.println(insertedAccount.toString());
        return insertedAccount;
    }

    public Account loginAccount(String username, String password) throws Exception {
        Account account = accountDAO.findAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            System.out.println("Login successfull" + account.toString());
            return account;
        } else {
            throw new Exception("Invalid username or password");
        }
    }
}
