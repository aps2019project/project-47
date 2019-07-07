package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.BattleChooseMenuController;
import models.Account;
import network.Requests.battle.OnlinePlayersRequest;
import network.Responses.Response;

import java.util.ArrayList;

public class OnlinePlayersResponse extends Response {
    private ArrayList<String > onlineUserNames = new ArrayList<>();
    public OnlinePlayersResponse(OnlinePlayersRequest request){
        this.request = request;
    }

    @Override
    public void handleRequest() {
        for (String authToken : Account.getAccountsMapper().keySet()){
            onlineUserNames.add(Account.getAccountsMapper().get(authToken).getUserName());
        }
    }

    @Override
    public void handleResponse() {
        BattleChooseMenuController.instance.addOtherPlayersUserNames(onlineUserNames);
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
