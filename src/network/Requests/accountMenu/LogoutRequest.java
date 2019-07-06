package network.Requests.accountMenu;

import network.ReqResType;
import network.Requests.Request;

public class LogoutRequest extends Request {
    public LogoutRequest(String authToken){
        this.authToken = authToken;
        this.reqResType = ReqResType.accountMenu;
    }

    @Override
    public ReqResType getReqResType() {
        return ReqResType.accountMenu;
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }
}

