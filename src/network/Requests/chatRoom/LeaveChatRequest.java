package network.Requests.chatRoom;

import network.ReqResType;
import network.Requests.Request;

public class LeaveChatRequest extends Request {
    public LeaveChatRequest(String authToken){
        this.authToken = authToken;
    }


}
