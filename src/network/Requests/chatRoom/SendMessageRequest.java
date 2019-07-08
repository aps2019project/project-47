package network.Requests.chatRoom;

import models.Message;
import network.Requests.Request;

public class SendMessageRequest extends Request {
    Message message;
    public SendMessageRequest(Message message, String authToken){
        this.authToken = authToken;
        this.message = message;
    }


    public Message getMessage() {
        return message;
    }
}
