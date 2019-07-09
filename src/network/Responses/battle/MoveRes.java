package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.BattleController;
import models.battle.board.Board;
import models.battle.board.Location;
import network.Responses.Response;

public class MoveRes extends Response {
    String minionId;
    Location location;

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {
        ((BattleController)Board.getController()).moveRes(minionId,location);
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }


}
