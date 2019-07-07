package network.Requests.battle;

import network.Requests.Request;

public class RejectNewGameRequest extends Request {
    String userName;
    public RejectNewGameRequest(String authToken, String userName){
        this.authToken = authToken;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
