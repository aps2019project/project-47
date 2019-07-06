package network.Responses.attack;

import controllers.Constants;
import controllers.MyController;
import controllers.console.AccountMenu;
import controllers.graphical.BattleController;
import models.Account;
import models.battle.board.Board;
import network.Client;
import network.Requests.accountMenu.LogoutRequest;
import network.Responses.Response;

public class EndTurnRes extends Response {

    @Override
    public void handleRequest() {

    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

    @Override
    public void handle(){
        ((BattleController)Board.getController()).endTurnRes();
    }
}
