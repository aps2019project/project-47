package network.Requests;

import network.ReqResType;

public class LoginRequest extends Request{
    private String userName;
    private String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.reqResType = ReqResType.LOGIN;
        this.authToken = null;
    }

    @Override
    public ReqResType getReqResType() {
        return ReqResType.LOGIN;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
