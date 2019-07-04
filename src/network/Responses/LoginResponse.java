package network.Responses;

import com.gilecode.yagson.YaGson;
import controllers.Constants;
import models.Account;
import network.Requests.LoginRequest;
import network.Requests.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoginResponse extends Response{
    private Account account = null;
    public LoginResponse(LoginRequest loginRequest){
        this.request = loginRequest;
    }
    @Override
    public synchronized void handleRequest() {
        String userName = ((LoginRequest)request).getUserName();
        String password = ((LoginRequest)request).getPassword();
        File accounts = new File("src/JSONs/Accounts/");
        for(File accountFile : accounts.listFiles()){
            if (accountFile.getName().startsWith(userName)){
                try {
                    Scanner scanner = new Scanner(accountFile);
                    Account account = new YaGson().fromJson(scanner.nextLine(), Account.class);
                    if (account.getPassword().equals(password)) {
                        this.account = account;
                        if (this.account.getAuthToken() != null) {
                            //todo and error in response that says an other device is logged in with this account
                        }
                        String authToken = Account.generateRandomString(32);
                        this.account.setAuthToken(authToken);
                        Account.putAccount(authToken, this.account);
                        requestResult = Constants.SUCCESSFUL_LOGIN;
                    }
                    else
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
}
