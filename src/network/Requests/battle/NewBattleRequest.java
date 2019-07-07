package network.Requests.battle;

import network.Requests.Request;

public class NewBattleRequest extends Request {
    String opponentUserName;
    public NewBattleRequest(String authToken, String opponentUserName){
        this.authToken = authToken;
        this.opponentUserName = opponentUserName;
    }
}
