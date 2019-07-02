package network.Requests;

import network.ReqResType;

public class CreateAccountRequest extends Request {
    private String userName;
    private String password;
    public CreateAccountRequest(String userName, String password){
        this.reqResType = ReqResType.CREATE;
        this.userName = userName;
        this.password = password;
        this.authToken = null;
    }

    @Override
    public ReqResType getReqResType() {
        return ReqResType.CREATE;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
