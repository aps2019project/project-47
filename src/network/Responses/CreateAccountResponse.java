package network.Responses;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.Constants;
import models.Account;
import network.Requests.CreateAccountRequest;
import network.Requests.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;

public class CreateAccountResponse extends Response {
    public CreateAccountResponse(CreateAccountRequest createAccountRequest){
        this.request = createAccountRequest;
    }
    @Override
    public void handleRequest(Request request){
        File accounts = new File("src/JSONs/Accounts");
        String userName = ((CreateAccountRequest) request).getUserName();
        String password = ((CreateAccountRequest) request).getPassword();
        for (File accountFile : accounts.listFiles()){
            if (accountFile.getName().equals(userName)){
                requestResult = Constants.ACCOUNT_EXISTS;
                break;
            }
        }


        Account account = new Account(userName, password);
        YaGsonBuilder gsonBuilder = new YaGsonBuilder();
        YaGson gson = gsonBuilder.create();
        try {
            Formatter formatter = new Formatter("src/JSONs/Accounts");
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

    @Override
    public void setRequestResult(Constants result) {

    }
}
