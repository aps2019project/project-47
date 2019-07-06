package network.Responses;

import controllers.Constants;
import models.Account;
import models.Message;
import network.Requests.chatRoom.UpdateChatRequest;
import network.Server;

import java.util.ArrayList;

public class UpdateChatResponse extends Response {
    private ArrayList<Message> messages = new ArrayList<>();

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public UpdateChatResponse(UpdateChatRequest request){
        this.request = request;
    }
    @Override
    public void handleRequest() {
        String authToken = request.getAuthToken();
        Account account = Account.getAccountsMapper().get(authToken);
        Integer index;
        index = Server.userLastMessageReceivedIndex.getOrDefault(
                account.
                getUserName(), 0);
        Server.userLastMessageReceivedIndex.put(account.getUserName(), Server.messages.size());
        for (int i = index; i < Server.messages.size(); i++){
            messages.add(Server.messages.get(i));
        }
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

    @Override
    public void handleResponse() {

    }
}
