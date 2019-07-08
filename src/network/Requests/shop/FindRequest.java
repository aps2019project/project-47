package network.Requests.shop;

import network.Requests.Request;

public class FindRequest extends Request {
    private int code;

    public FindRequest(String token, int code) {
        this.authToken = token;
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
