package network.Responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.Constants;
import models.Account;
import network.Requests.CreateAccountRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;

public class CreateAccountResponse extends Response {
    public CreateAccountResponse(CreateAccountRequest createAccountRequest){
        this.request = createAccountRequest;
    }
    @Override
    public void handleRequest(){
        File accounts = new File("src/JSONs/Accounts");
        String userName = ((CreateAccountRequest) request).getUserName();
        String password = ((CreateAccountRequest) request).getPassword();
        for (File accountFile : accounts.listFiles()){
            if (accountFile.getName().startsWith(userName)){
                requestResult = Constants.ACCOUNT_EXISTS;
                break;
            }
        }


        Account account = new Account(userName, password);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            Formatter formatter = new Formatter("src/JSONs/Accounts/" + userName + ".json");
            formatter.format(gson.toJson(account));
            requestResult = Constants.ACCOUNT_CREATE_SUCCESSFULLY;
            formatter.flush();
            formatter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Constants getRequestResult() {
        return this.requestResult;
    }
}
