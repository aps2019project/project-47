package network.Requests;

import network.ReqResType;

public class LogoutRequest extends Request{
    public LogoutRequest(String authToken){
        this.authToken = authToken;
        this.reqResType = ReqResType.LOGOUT;
    }

    @Override
    public ReqResType getReqResType() {
        return ReqResType.LOGOUT;
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }
}

