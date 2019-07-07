package network.Responses.battle;

import controllers.Constants;
import controllers.console.MainMenu;
import network.Client;
import network.Requests.battle.CancelNewBattleRequest;
import network.Responses.Response;

public class CancelNewBattleResponse extends Response {
    public CancelNewBattleResponse(CancelNewBattleRequest cancelNewBattleRequest) {
        this.request = cancelNewBattleRequest;
    }

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
