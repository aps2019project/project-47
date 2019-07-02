package network.Responses;

import controllers.Constants;
import network.Requests.LoginRequest;
import network.Requests.Request;

import java.io.File;

public class LoginResponse extends Response{
    public LoginResponse(LoginRequest loginRequest){
        this.request = loginRequest;
    }


    public void handleRequest() {
        String userName = ((LoginRequest)request).getUserName();
        String password = ((LoginRequest)request).getPassword();
        File accounts = new File("src/JSONs/Accounts/");
        for(File account : accounts.listFiles()){
            if (account.getName().startsWith(userName)){

            }
        }
    }

    @Override
    public void handleRequest(Request request) {

    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

    @Override
    public void setRequestResult(Constants result) {

    }
}
