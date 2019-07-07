package network.Requests.account;

import models.Account;
import network.Requests.Request;

public class UpdateAccountRequest extends Request {

    public Account account;

    public UpdateAccountRequest(Account loginAccount) {
        this.authToken = loginAccount.getAuthToken();
        this.account = loginAccount;
    }
}
