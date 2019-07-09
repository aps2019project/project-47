package network.Requests.battle;

import models.battle.MatchType;
import network.Requests.Request;

public class NewBattleRequest extends Request {
    private String opponentUserName;
    private MatchType matchType;
    private int numOfFlags;
    public NewBattleRequest(String authToken, String opponentUserName, MatchType matchType, int numOfFlags){
        this.authToken = authToken;
        this.opponentUserName = opponentUserName;
        this.matchType = matchType;
        this.numOfFlags = numOfFlags;
    }

    public String getOpponentUserName() {
        return opponentUserName;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public int getNumOfFlags() {
        return numOfFlags;
    }
}
