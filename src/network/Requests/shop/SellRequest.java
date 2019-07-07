package network.Requests.shop;

import network.ReqResType;
import network.Requests.Request;

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
