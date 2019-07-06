package network.Requests.accountMenu;

import network.ReqResType;
import network.Requests.Request;

public class LoginRequest extends Request {
    private String userName;
    private String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.reqResType = ReqResType.accountMenu;
        this.authToken = null;
    }

    @Override
    public ReqResType getReqResType() {
        return ReqResType.accountMenu;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
