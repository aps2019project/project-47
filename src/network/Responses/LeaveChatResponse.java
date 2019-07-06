package network.Responses;

import controllers.Constants;
import controllers.graphical.GlobalChatController;
import network.ReqResType;
import network.Requests.Request;
import network.Requests.chatRoom.LeaveChatRequest;
import network.Server;

public class LeaveChatResponse extends Response {
    public LeaveChatResponse(LeaveChatRequest request){
        this.request = request;
    }

    @Override
    public void handleRequest() {
        Server.userLastMessageReceivedIndex.remove(request.getAuthToken());
    }

    @Override
    public void handleResponse() {
        GlobalChatController.instance.goToMainMenu();
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
