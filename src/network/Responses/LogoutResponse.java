package network.Responses;

import controllers.Constants;
import controllers.console.AccountMenu;
import models.Account;
import network.Client;
import network.Requests.accountMenu.LogoutRequest;

public class LogoutResponse extends Response{
    public LogoutResponse(LogoutRequest logoutRequest){
        this.request = logoutRequest;
    }

    @Override
    public void handleRequest() {
        if (Account.getAccountsMapper().containsKey(request.getAuthToken())){
            Account.getAccountsMapper().get(request.getAuthToken()).setAuthToken(null);
            Account.getAccountsMapper().remove(request.getAuthToken());
        }
    }

    @Override
    public Constants getRequestResult() {
        AccountMenu.setLoginAccount(null);
        Client.getStage().getScene().setRoot(AccountMenu.getRoot());
        return null;
    }
}
