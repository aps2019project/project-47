package network.Requests.shop;

import network.ReqResType;
import network.Requests.Request;

public class FindRequest extends Request {
    private int code;

    @Override
    public ReqResType getReqResType() {
        return ReqResType.shop;
    }

    public FindRequest(String token, int code) {
        this.authToken = token;
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
