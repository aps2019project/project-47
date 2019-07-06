package network.Responses;

import controllers.Constants;
import models.Account;
import models.Message;
import network.Requests.chatRoom.SendMessageRequest;
import network.Server;

public class SendMessageResponse extends Response{
    public SendMessageResponse(SendMessageRequest request){
        this.request = request;
    }

    @Override
    public void handleRequest() {
        if (Account.getAccountsMapper().containsKey(request.getAuthToken())) {
            Message message = ((SendMessageRequest) request).getMessage();
            Server.messages.add(message);
        }
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
