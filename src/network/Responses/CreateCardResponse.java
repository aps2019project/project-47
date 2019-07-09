package network.Responses;

import controllers.Constants;
import controllers.graphical.ServerShopController;
import models.Shop;
import network.Requests.Request;
import network.Requests.shop.CreateCardRequest;

public class CreateCardResponse extends Response {


    public CreateCardResponse(Request request) {
        this.request = request;
    }

    @Override
    public void handleRequest() {
        Shop.getInstance().getCards().put(((CreateCardRequest) request).getCard(), 1);
        ServerShopController.instance.addCard(((CreateCardRequest) request).getCard());
    }

    @Override
    public void handleResponse() {

    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
