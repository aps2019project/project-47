package network.Requests.battle;

import models.battle.MatchType;
import network.Requests.Request;

public class NewBattleRequest extends Request {
    private String opponentUserName;
    private MatchType matchType;
    private int numOfFlags;
    private double turnTime;
    public NewBattleRequest(String authToken, String opponentUserName, MatchType matchType, int numOfFlags, double turnTime){
        this.authToken = authToken;
        this.opponentUserName = opponentUserName;
        this.matchType = matchType;
        this.numOfFlags = numOfFlags;
        this.turnTime = turnTime;
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

    public double getTurnTime() {
        return turnTime;
    }
}
