package network.Requests.shop;

import network.Requests.Request;

public class BuyRequest extends Request {

    private int code;

    public BuyRequest(String token, int code) {
        this.authToken = token;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
