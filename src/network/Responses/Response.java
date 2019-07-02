package network.Responses;

import controllers.Constants;
import network.Requests.Request;
import network.ReqResType;

public abstract class Response{
    protected ReqResType type;
    protected Request request;
    protected Constants requestResult;
    public abstract void handleRequest(Request request);
    public abstract Constants getRequestResult();
    public abstract void setRequestResult(Constants result);
}
