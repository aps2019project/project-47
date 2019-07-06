package network.Responses;

import controllers.Constants;
import controllers.console.AccountMenu;
import controllers.graphical.UniversalShopController;
import models.Account;
import models.Shop;
import network.Requests.shop.BuyRequest;
import network.Server;

public class BuyResponse extends Response {

    public BuyResponse(BuyRequest buyRequest){
        this.request = buyRequest;
    }

    Shop shop;

    @Override
    public synchronized void handleRequest() {
        int code = ((BuyRequest) request).getCode();
        shop = Shop.getInstance();
        AccountMenu.setLoginAccount(Account.getAccountsMapper().get(request.getAuthToken()));
        Constants resultCode = Server.getShop().command_buy(code);
        requestResult = resultCode;
        AccountMenu.setLoginAccount(null);
    }

    @Override
    public Constants getRequestResult() {
        return requestResult;
    }

    @Override
    public synchronized void handleResponse() {
        UniversalShopController.instance.showBuyResponse(this);
        Shop.setOurInstance(shop);
    }
}