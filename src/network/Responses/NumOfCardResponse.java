package network.Responses;

import controllers.Constants;

public class NumOfCardResponse extends Response {

    @Override
    public synchronized void handleRequest() {

    }

    @Override
    public Constants getRequestResult() {
        return Constants.NUM_OF_CARD_RESPONSE;
    }

    @Override
    public void handleResponse() {

    }
}
