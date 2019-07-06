package network.Requests;

import network.ReqResType;

public class SellRequest extends Request {

    private int code;

    public SellRequest(String token, int code) {
        this.authToken = token;
        this.code = code;
    }

    public int getCode() {
        return code;
    }


}
