package network.Responses.battle;

import controllers.Constants;
import network.Requests.battle.BattleActionRequest;
import network.Responses.Response;

public class BattleActionResponse extends Response {

    public BattleActionResponse(BattleActionRequest request) {
        this.request = request;
    }

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {

    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
