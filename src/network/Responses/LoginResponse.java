package network.Responses;

import com.gilecode.yagson.YaGson;
import controllers.Constants;
import controllers.console.AccountMenu;
import controllers.graphical.LoginRegisterController;
import models.Account;
import network.Requests.account.LoginRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class LoginResponse extends Response {
    private Account account = null;

    public LoginResponse(LoginRequest loginRequest) {
        this.request = loginRequest;
    }

    @Override
    public synchronized void handleRequest() {
        String userName = ((LoginRequest) request).getUserName();
        String password = ((LoginRequest) request).getPassword();
        File accounts = new File("src/JSONs/Accounts/");
        for (File accountFile : Objects.requireNonNull(accounts.listFiles())) {
            if (accountFile.getName().startsWith(userName)) {
                try {
                    Scanner scanner = new Scanner(accountFile);
                    Account account = new YaGson().fromJson(scanner.nextLine(), Account.class);
                    if (account.getPassword().equals(password)) {
                        this.account = account;
                        ArrayList<Account> accounts1 = new ArrayList<>(Account.getAccountsMapper().values());
                        boolean isLoggedin = false;
                        for (Account account1 : accounts1) {
                            if (account1.getUserName().equals(account.getUserName())){
                                isLoggedin = true;
                                break;
                            }
                        }
                        if (isLoggedin) {
                            requestResult = Constants.ACCOUNT_LOGGED_IN;
                            break;
                        }
                        String authToken = Account.generateRandomString(32);
                        this.account.setAuthToken(authToken);
                        Account.putAccount(authToken, this.account);
                        requestResult = Constants.SUCCESSFUL_LOGIN;
                    } else
                        requestResult = Constants.WRONG_PASSWORD;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (requestResult == null)
            requestResult = Constants.INVALID_USERNAME;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public Constants getRequestResult() {
        return requestResult;
    }

    @Override
    public void handleResponse() {
        AccountMenu.setLoginAccount(account);
        LoginRegisterController.instance.login(this);
    }
}
