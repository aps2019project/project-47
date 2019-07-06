package network.Responses.attack;

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
    public Constants getRequestResult() {
        return null;
    }

    @Override
    public void handle(){
        ((BattleController)Board.getController()).useSpecialPowerRes(heroId,target);
    }
}
