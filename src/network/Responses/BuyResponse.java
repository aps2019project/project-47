package network.Responses;

import controllers.Constants;
import controllers.console.AccountMenu;
import network.Requests.BuyRequest;
import network.Requests.Request;
import network.Server;

public class BuyResponse extends Response {

    @Override
    public void handleRequest(Request request) {
        int code = ((BuyRequest) request).getCode();
        AccountMenu.setLoginAccount(Server.getTokens().get(request.getAuthToken()));
        Constants resultCode = Server.getShop().command_buy(code);
        setRequestResult(resultCode);
        AccountMenu.setLoginAccount(null);
    }

    @Override
    public Constants getRequestResult() {
        return requestResult;
    }

    @Override
    public void setRequestResult(Constants result) {
        this.requestResult = result;
    }
}
