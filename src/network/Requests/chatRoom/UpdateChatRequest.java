package network.Requests.chatRoom;

import network.ReqResType;
import network.Requests.Request;

public class UpdateChatRequest extends Request {
    public UpdateChatRequest(String authToken){
        this.authToken = authToken;
    }

}
