package network.Responses;

import network.Requests.LoginRequest;

import java.io.File;

public class LoginResponse extends Response{
    public LoginResponse(LoginRequest loginRequest){
        this.request = loginRequest;
    }

    @Override
    public void handleRequest() {
        String userName = ((LoginRequest)request).getUserName();
        String password = ((LoginRequest)request).getPassword();
        File accounts = new File("src/JSONs/Accounts/");
        for(File account : accounts.listFiles()){
            if (account.getName().startsWith(userName)){

            }
        }
    }
}
