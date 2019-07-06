package network.Responses;

import controllers.Constants;
import controllers.graphical.MainMenuController;
import models.Account;
import network.Requests.account.LogoutRequest;

public class LogoutResponse extends Response {
    public LogoutResponse(LogoutRequest logoutRequest) {
        this.request = logoutRequest;
    }

    @Override
    public void handleRequest() {
        if (Account.getAccountsMapper().containsKey(request.getAuthToken())) {
            Account.getAccountsMapper().get(request.getAuthToken()).setAuthToken(null);
            Account.getAccountsMapper().remove(request.getAuthToken());
        }
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

    @Override
    public void handleResponse() {
        MainMenuController.instance.doLogOut();
        System.out.println("alan toye handle response am");
    }
}
