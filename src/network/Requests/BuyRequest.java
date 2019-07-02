package network.Requests;

import network.ReqResType;

public class BuyRequest extends Request {

    private int code;

    public BuyRequest(String token, int code) {
        this.authToken = token;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public ReqResType getReqResType() {
        return ReqResType.BUY;
    }
    
}
