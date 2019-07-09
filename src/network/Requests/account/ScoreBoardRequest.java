package network.Requests.account;

import network.Requests.Request;

public class ScoreBoardRequest extends Request {
    public ScoreBoardRequest(String authToken){
        this.authToken = authToken;
    }
}
