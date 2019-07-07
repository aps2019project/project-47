package network.Responses;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import controllers.Constants;
import controllers.console.AccountMenu;
import controllers.graphical.LoginRegisterController;
import models.Account;
import network.Requests.account.CreateAccountRequest;

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
            if (accountFile.getName().startsWith(userName) && accountFile.getName().substring(userName.length()).charAt(0) == '.'){
                requestResult = Constants.ACCOUNT_EXISTS;
                break;
            }
        }
        if (userName.toLowerCase().equals("pc")){
            requestResult = Constants.PC_NOT_ALLOWED;
            return;
        }
        if (requestResult != Constants.ACCOUNT_EXISTS) {
            Account account = new Account(userName, password);
            AccountMenu.addAccount(account);
            YaGsonBuilder gsonBuilder = new YaGsonBuilder();
            YaGson gson = gsonBuilder.create();
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
    }

    @Override
    public Constants getRequestResult() {
        return this.requestResult;
    }

    @Override
    public void handleResponse() {
        LoginRegisterController.instance.createAccount(requestResult);
    }
}
