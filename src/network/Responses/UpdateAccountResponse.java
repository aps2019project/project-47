package network.Responses;

import controllers.Constants;
import controllers.console.AccountMenu;
import models.Account;
import network.Requests.account.UpdateAccountRequest;

public class UpdateAccountResponse extends Response {

    public UpdateAccountResponse(UpdateAccountRequest request) {
        Account.getAccountsMapper().put(request.getAuthToken(), request.account);
    }

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {

    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
