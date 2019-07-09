package network.Requests.battle;

import models.battle.MatchType;
import network.Requests.Request;

public class StartNewBattleRequest extends Request {
    private String userNameOfOpponent;
    private MatchType matchType;
    private int numberOfFlags;

    public StartNewBattleRequest(String authToken, String userNameOfOpponent, MatchType matchType, int numberOfFlags) {
        this.authToken = authToken;// dovomi
        this.userNameOfOpponent = userNameOfOpponent; //avali
        this.matchType = matchType;
        this.numberOfFlags = numberOfFlags;
    }

    public String getUserNameOfOpponent() {
        return userNameOfOpponent;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public int getNumberOfFlags() {
        return numberOfFlags;
    }
}
