package network.Requests.account;

import network.Requests.Request;

public class LoginRequest extends Request {
    private String userName;
    private String password;

    public LoginRequest(String userName, String password) {
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