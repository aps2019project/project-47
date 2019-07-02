package network.Requests;

import network.ReqResType;

public class FindRequest extends Request {
    private int code;

    @Override
    public ReqResType getReqResType() {
        return ReqResType.FIND;
    }

    public FindRequest(String token, int code) {
        this.authToken = token;
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
