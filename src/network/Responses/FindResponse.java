package network.Responses;

import controllers.Constants;
import models.Account;
import models.Shop;
import network.Requests.shop.FindRequest;

public class FindResponse extends Response {
    private Object cardOrItem;
    public FindResponse(FindRequest findRequest){
        this.request = findRequest;
    }
    @Override
    public synchronized void handleRequest() {
        if (Account.getAccountsMapper().get(request.getAuthToken()) == null)
            return;
        cardOrItem = Shop.getInstance().find_in_shop(((FindRequest) request).getCode());
    }

    public Object getCardOrItem() {
        return cardOrItem;
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

    @Override
    public void handleResponse() {

    }
}
