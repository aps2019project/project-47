package network.Requests.battle;

import network.Requests.Request;

public class CancelNewBattleRequest extends Request {
    private String opponentUserName;
    public CancelNewBattleRequest(String authToken, String opponentUserName){
        this.authToken = authToken;
        this.opponentUserName = opponentUserName;
    }

    public String getOpponentUserName() {
        return opponentUserName;
    }
}
