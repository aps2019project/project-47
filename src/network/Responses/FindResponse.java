package network.Responses;

import controllers.Constants;
import models.Shop;
import network.Requests.FindRequest;
import network.Requests.Request;
import network.Server;

public class FindResponse extends Response {
    private Object cardOrItem;

    @Override
    public void handleRequest(Request request) {
        if (Server.getTokens().get(request.getAuthToken()) == null)
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
    public void setRequestResult(Constants result) {

    }
}
