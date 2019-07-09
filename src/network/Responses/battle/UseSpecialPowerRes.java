package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.BattleController;
import models.battle.board.Board;
import models.battle.board.Location;
import network.Responses.Response;

public class UseSpecialPowerRes extends Response {
    String heroId;
    Location target;

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {
        ((BattleController)Board.getController()).useSpecialPowerRes(heroId,target);
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }


}
