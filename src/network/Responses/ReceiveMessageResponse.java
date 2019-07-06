package network.Responses;

import controllers.Constants;
import controllers.graphical.GlobalChatController;
import models.Message;

public class ReceiveMessageResponse extends Response {
    private Message message;
    public ReceiveMessageResponse(Message message){
        this.message = message;
    }

    @Override
    public void handleRequest() {

    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public void handleResponse() {
        GlobalChatController.instance.addNewMessage(message);
    }
}
