package network.Requests;

import models.Message;
import network.ReqResType;

public class SendMessageRequest extends Request{
    Message message;
    public SendMessageRequest(Message message, String authToken){
        this.authToken = authToken;
        this.message = message;
    }

    @Override
    public ReqResType getReqResType() {
        return null;
    }

    public Message getMessage() {
        return message;
    }
}
