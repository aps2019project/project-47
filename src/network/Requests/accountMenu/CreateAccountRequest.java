package network.Requests.accountMenu;

import network.ReqResType;
import network.Requests.Request;

public class CreateAccountRequest extends Request {
    private String userName;
    private String password;
    public CreateAccountRequest(String userName, String password){
        this.reqResType = ReqResType.accountMenu;
        this.userName = userName;
        this.password = password;
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
