package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.BattleController;
import models.Account;
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
        BattleController.instance.doOneAction(((BattleActionRequest) request).getBattleAction());
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
