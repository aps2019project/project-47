package network.Responses;

import network.Requests.CreateAccountRequest;

import java.io.File;

public class CreateAccountResponse extends Response {
    public CreateAccountResponse(CreateAccountRequest createAccountRequest){
        this.request = createAccountRequest;
    }
    public void handleRequest(){
        File accounts = new File("/JSONs/Accounts");
        for (File accountFile : accounts.listFiles()){
            if (accountFile.getName().equals(((CreateAccountRequest)request).getUserName())){

            }
        }
    }
}
