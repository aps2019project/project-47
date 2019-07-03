package network.Responses;

import controllers.Constants;
import models.Shop;
import network.Requests.FindRequest;
import network.Requests.Request;
import network.Server;

public class FindResponse extends Response {
    private Object cardOrItem;
    public FindResponse(FindRequest findRequest){
        this.request = findRequest;
    }
    @Override
    public void handleRequest() {
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
}
