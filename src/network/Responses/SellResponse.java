package network.Responses;

import controllers.Constants;
import controllers.console.AccountMenu;
import controllers.graphical.UniversalShopController;
import models.Account;
import models.Shop;
import network.Requests.shop.SellRequest;

public class SellResponse extends Response {

    public SellResponse(SellRequest sellRequest) {
        this.request = sellRequest;
    }

    Shop shop;
    Account account;

    @Override
    public void handleRequest() {
        AccountMenu.setLoginAccount(Account.getAccountsMapper().get(request.getAuthToken()));
        if (Account.getAccountsMapper().get(request.getAuthToken()) != null)
            Shop.getInstance().command_sell(((SellRequest) request).getCode());
        shop = Shop.getInstance();
        account = Account.getAccountsMapper().get(request.getAuthToken());
        AccountMenu.setLoginAccount(null);
    }

    @Override
    public void handleResponse() {
        Shop.setOurInstance(shop);
        AccountMenu.setLoginAccount(account);
        UniversalShopController.instance.setLoginAccount(account);
        UniversalShopController.instance.getMoney().setText("Money: " + account.getMoney());
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
