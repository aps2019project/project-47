package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.BattleController;
import models.battle.board.Board;
import models.battle.board.Location;
import network.Responses.Response;

public class InsertRes extends Response {
    String cardId;
    Location target;

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {
        ((BattleController)Board.getController()).insertRes(cardId, target);
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }


}
