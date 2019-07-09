package network.Requests.account;

import network.Requests.Request;

public class CreateAccountRequest extends Request {
    private String userName;
    private String password;
    public CreateAccountRequest(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.authToken = null;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
