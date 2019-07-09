package network.Responses;

import controllers.Constants;
import controllers.console.AccountMenu;
import controllers.graphical.ServerShopController;
import controllers.graphical.UniversalShopController;
import models.Account;
import models.Shop;
import network.Requests.shop.BuyRequest;
import network.Server;

public class BuyResponse extends Response {

    public BuyResponse(BuyRequest buyRequest) {
        this.request = buyRequest;
    }

    Shop shop;
    Account account;

    @Override
    public synchronized void handleRequest() {
        shop = Shop.getInstance();
        account = Account.getAccountsMapper().get(request.getAuthToken());
        AccountMenu.setLoginAccount(Account.getAccountsMapper().get(request.getAuthToken()));
        if (request != null) {
            int code = ((BuyRequest) request).getCode();
            requestResult = Shop.getInstance().command_buy(code);
            ServerShopController.instance.updateTable();
        } else {
            requestResult = Constants.NULL_REQUEST;
        }
        AccountMenu.setLoginAccount(null);
    }

    @Override
    public Constants getRequestResult() {
        return requestResult;
    }

    @Override
    public synchronized void handleResponse() {
        Shop.setOurInstance(shop);
        AccountMenu.setLoginAccount(account);
        UniversalShopController.instance.setLoginAccount(account);
        UniversalShopController.instance.showBuyResponse(this);
    }
}