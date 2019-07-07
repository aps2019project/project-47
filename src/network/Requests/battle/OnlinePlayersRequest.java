package network.Requests.battle;

import network.Requests.Request;

public class OnlinePlayersRequest extends Request {
    public OnlinePlayersRequest(String authToken){
        this.authToken = authToken;
    }

}
