package network.Responses;

import com.gilecode.yagson.YaGson;
import controllers.Constants;
import controllers.console.AccountMenu;
import controllers.graphical.ScoreBoardController;
import models.Account;
import network.Requests.account.ScoreBoardRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoreBoardResponse extends Response{
    private ArrayList<Account> accounts;
    public ScoreBoardResponse(ScoreBoardRequest request){
        this.request = request;
    }
    @Override
    public void handleRequest() {
        YaGson yaGson = new YaGson();
        File file = new File("src/JSONs/Accounts/");
        for (File file1 : file.listFiles()){
            try {
                Scanner scanner = new Scanner(file1);
                Account account = yaGson.fromJson(scanner.nextLine(), Account.class);
                AccountMenu.getAccounts().add(account);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        AccountMenu.sortAccounts();
        this.accounts = AccountMenu.getAccounts();
    }

    @Override
    public void handleResponse() {
        for (int i = 0; i < accounts.size(); i++) {
            ScoreBoardController.instance.addDetails(accounts.get(i), i+1);
        }
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
