package network.Responses;

import controllers.Constants;
import controllers.console.AccountMenu;
import models.Account;
import network.Requests.BuyRequest;
import network.Requests.Request;
import network.Server;

public class BuyResponse extends Response {

    @Override
    public void handleRequest() {
        int code = ((BuyRequest) request).getCode();
        AccountMenu.setLoginAccount(Account.getAccountsMapper().get(request.getAuthToken()));
        Constants resultCode = Server.getShop().command_buy(code);
        requestResult = resultCode;
        AccountMenu.setLoginAccount(null);
    }

    @Override
    public Constants getRequestResult() {
        return requestResult;
    }

}
