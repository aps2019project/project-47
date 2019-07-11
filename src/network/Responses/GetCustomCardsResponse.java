package network.Responses;

import controllers.Constants;
import controllers.graphical.UniversalShopController;
import models.cards.Card;
import network.Server;

import java.util.ArrayList;

public class GetCustomCardsResponse extends Response {

    public ArrayList<Card> customCards = new ArrayList<>();

    @Override
    public void handleRequest() {
        customCards = Server.customCards;
    }

    @Override
    public void handleResponse() {
        customCards.forEach(card -> {
            UniversalShopController.instance.addId(card);
        });
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
