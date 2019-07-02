package network.Requests;

import network.ReqResType;

public abstract class Request {
    protected ReqResType reqResType;
    protected String authToken;
    public abstract ReqResType getReqResType();

    public String getAuthToken() {
        return authToken;
    }
}
