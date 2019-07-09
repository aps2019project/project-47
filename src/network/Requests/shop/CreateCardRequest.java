package network.Requests.shop;

import models.cards.Card;
import network.Requests.Request;

public class CreateCardRequest extends Request {
    public Card getCard() {
        return card;
    }

    private Card card;

    public CreateCardRequest(Card card) {
        this.card = card;
    }
}
