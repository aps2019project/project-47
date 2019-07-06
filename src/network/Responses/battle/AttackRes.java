package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.BattleController;
import models.battle.board.Board;
import network.Requests.account.LogoutRequest;
import network.Responses.Response;

public class AttackRes extends Response {
    String attackerId;
    String defenerId;

    public AttackRes(LogoutRequest logoutRequest) {


    }

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {
        ((BattleController)Board.getController()).attackRes(attackerId,defenerId);
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

    @Override
    public void handle(){

    }
}
