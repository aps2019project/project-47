package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.BattleChooseMenuController;
import network.Responses.Response;

public class RejectNewBattleResponse extends Response {
    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {
        BattleChooseMenuController.instance.doCancel();
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
