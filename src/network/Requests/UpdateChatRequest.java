package network.Requests;

import network.ReqResType;

public class UpdateChatRequest extends Request{
    public UpdateChatRequest(String authToken){
        this.authToken = authToken;
    }
    @Override
    public ReqResType getReqResType() {
        return null;
    }
}
