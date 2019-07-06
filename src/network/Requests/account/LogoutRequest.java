package network.Requests.account;

import network.ReqResType;
import network.Requests.Request;

public class LogoutRequest extends Request {
    public LogoutRequest(String authToken){
        this.authToken = authToken;
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }
}

