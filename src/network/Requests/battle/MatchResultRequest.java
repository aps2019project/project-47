package network.Requests.battle;

import models.battle.MatchResult;
import network.Requests.Request;

public class MatchResultRequest extends Request {
    private MatchResult matchResult;
    public MatchResultRequest(String authToken, MatchResult matchResult){
        this.authToken = authToken;
        this.matchResult = matchResult;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }
}
