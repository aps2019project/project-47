package network.Requests.battle;

import network.Requests.Request;

public class UpdateScoreBoardRequest extends Request {
    public UpdateScoreBoardRequest(String authToken){
        this.authToken = authToken;
    }
}
