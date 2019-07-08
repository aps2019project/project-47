package network.Requests.battle;

import network.Requests.Request;

public class NewBattleRequest extends Request {
    private String opponentUserName;
    public NewBattleRequest(String authToken, String opponentUserName){
        this.authToken = authToken;
        this.opponentUserName = opponentUserName;
    }

    public String getOpponentUserName() {
        return opponentUserName;
    }
}