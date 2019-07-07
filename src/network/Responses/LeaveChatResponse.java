package network.Responses;

import controllers.Constants;
import controllers.graphical.GlobalChatController;
import models.Account;
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
        Server.userLastMessageReceivedIndex.remove(Account.getAccountsMapper().get(request.getAuthToken()).getUserName());
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
