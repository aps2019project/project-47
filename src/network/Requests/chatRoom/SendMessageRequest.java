package network.Requests.chatRoom;

import models.Message;
import network.ReqResType;
import network.Requests.Request;

public class SendMessageRequest extends Request {
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
