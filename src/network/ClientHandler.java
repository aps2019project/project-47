package network;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import controllers.Constants;
import models.Account;
import models.Shop;
import network.Requests.*;
import network.Requests.account.CreateAccountRequest;
import network.Requests.account.LoginRequest;
import network.Requests.account.LogoutRequest;
import network.Requests.account.UpdateAccountRequest;
import network.Requests.battle.*;
import network.Requests.chatRoom.LeaveChatRequest;
import network.Requests.chatRoom.SendMessageRequest;
import network.Requests.chatRoom.UpdateChatRequest;
import network.Requests.shop.BuyRequest;
import network.Requests.shop.FindRequest;
import network.Requests.shop.SellRequest;
import network.Responses.*;
import network.Responses.battle.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    public static YaGsonBuilder gsonBuilder = new YaGsonBuilder();
    public static YaGson gson = gsonBuilder.create();
    private PrintWriter out;
    private Scanner scanner;

    public PrintWriter getOut() {
        return out;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public ClientHandler(Socket socket) throws IOException {
        out = new PrintWriter(socket.getOutputStream());
        scanner = new Scanner(socket.getInputStream());
    }

    @Override
    public void run() {
        while (scanner.hasNextLine()) {
            String str = this.scanner.nextLine();
            Request request = gson.fromJson(str, Request.class);
            String responseStr = "";
            if (request instanceof BuyRequest) {
                BuyResponse buyResponse = new BuyResponse((BuyRequest) request);
                buyResponse.handleRequest();
                responseStr = gson.toJson(buyResponse);
                out.println(responseStr);
                out.flush();
                continue;
            }
            if (request instanceof FindRequest) {
                FindResponse findResponse = new FindResponse((FindRequest) request);
                findResponse.handleRequest();
                responseStr = gson.toJson(findResponse);
                out.println(responseStr);
                out.flush();
                continue;
            }
            if (request instanceof SellRequest) {
                SellResponse sellResponse = new SellResponse((SellRequest) request);
                sellResponse.handleRequest();
                responseStr = gson.toJson(sellResponse);
                out.println(responseStr);
                out.flush();
                continue;
            }
            if (request instanceof CreateAccountRequest) {
                CreateAccountResponse createAccountResponse = new CreateAccountResponse((CreateAccountRequest) request);
                createAccountResponse.handleRequest();
                responseStr = gson.toJson(createAccountResponse);
                out.println(responseStr);
                out.flush();
                continue;
            }
            if (request instanceof LoginRequest) {
                LoginResponse loginResponse = new LoginResponse((LoginRequest) request);
                loginResponse.handleRequest();
                responseStr = gson.toJson(loginResponse);
                out.println(responseStr);
                out.flush();
                if (loginResponse.getRequestResult() == Constants.SUCCESSFUL_LOGIN) {
                    Server.clientHandlers.put(loginResponse.getAccount().getUserName(), this);
                }
                showOnlineAccounts();
                continue;
            }
            if (request instanceof LogoutRequest) {
                LogoutResponse logoutResponse = new LogoutResponse((LogoutRequest) request);
                Server.clientHandlers.remove(Account.getAccountsMapper().get(request.getAuthToken()).getUserName());
                logoutResponse.handleRequest();
                responseStr = gson.toJson(logoutResponse);
                showOnlineAccounts();
                out.println(responseStr);
                out.flush();
                continue;
            }
            if (request instanceof UpdateAccountRequest){
                new UpdateAccountResponse((UpdateAccountRequest) request);
            }
            if (request instanceof UpdateChatRequest) {
                UpdateChatResponse updateChatResponse = new UpdateChatResponse((UpdateChatRequest) request);
                updateChatResponse.handleRequest();
                responseStr = gson.toJson(updateChatResponse);
                out.println(responseStr);
                out.flush();
                continue;
            }
            if (request instanceof SendMessageRequest) {
                SendMessageResponse sendMessageResponse = new SendMessageResponse((SendMessageRequest) request);
                sendMessageResponse.handleRequest();
                ReceiveMessageResponse receiveMessageResponse = new ReceiveMessageResponse(((SendMessageRequest) request).getMessage());
                String receiveMessageResponseStr = gson.toJson(receiveMessageResponse);

                broadcastMessage(receiveMessageResponseStr);

                for(String authToken : Server.userLastMessageReceivedIndex.keySet()){
                    Server.increamentMessageIndex(authToken);
                }
                continue;
            }
            if (request instanceof LeaveChatRequest) {
                LeaveChatResponse leaveChatResponse = new LeaveChatResponse((LeaveChatRequest) request);
                leaveChatResponse.handleRequest();
                responseStr = gson.toJson(leaveChatResponse);
                out.println(responseStr);
                out.flush();
                continue;
            }

            if (request instanceof OnlinePlayersRequest){
                OnlinePlayersResponse onlinePlayersResponse = new OnlinePlayersResponse((OnlinePlayersRequest) request);
                onlinePlayersResponse.handleRequest();
                responseStr = gson.toJson(onlinePlayersResponse);
                out.println(responseStr);
                out.flush();
                continue;
            }
            if (request instanceof NewBattleRequest){
                AcceptancePageResponse acceptancePageResponse = new AcceptancePageResponse((NewBattleRequest) request);
                acceptancePageResponse.handleRequest();
                ClientHandler clientHandler = Server.clientHandlers.get(((NewBattleRequest) request).getOpponentUserName());
                responseStr = gson.toJson(acceptancePageResponse);
                clientHandler.out.println(responseStr);
                clientHandler.out.flush();
                continue;
            }
            if (request instanceof CancelNewBattleRequest){
                CancelNewBattleResponse cancelNewBattleResponse = new CancelNewBattleResponse((CancelNewBattleRequest) request);
                responseStr = gson.toJson(cancelNewBattleResponse);
                ClientHandler clientHandler = Server.clientHandlers.get(((CancelNewBattleRequest) request).getOpponentUserName());
                clientHandler.out.println(responseStr);
                clientHandler.out.flush();
                continue;
            }
            if (request instanceof RejectNewGameRequest){
                RejectNewBattleResponse rejectNewBattleResponse = new RejectNewBattleResponse();
                responseStr = gson.toJson(rejectNewBattleResponse);
                ClientHandler clientHandler = Server.clientHandlers.get(((RejectNewGameRequest) request).getUserName());
                clientHandler.out.println(responseStr);
                clientHandler.out.flush();
                continue;
            }
            if(request instanceof StartNewBattleRequest){
                StartNewBattleResponse startNewBattleResponse = new StartNewBattleResponse((StartNewBattleRequest) request);
                startNewBattleResponse.handleRequest();
                ClientHandler clientHandler1 = Server.clientHandlers.get(((StartNewBattleRequest) request).getUserNameOfOpponent());
                String player2UserName = Account.getAccountsMapper().get(request.getAuthToken()).getUserName();
                ClientHandler clientHandler2 = Server.clientHandlers.get(player2UserName);
                startNewBattleResponse.setWhoIsUnHuman(false);
                responseStr = gson.toJson(startNewBattleResponse);
                clientHandler1.out.println(responseStr);
                clientHandler1.out.flush();
                startNewBattleResponse.setWhoIsUnHuman(true);
                responseStr = gson.toJson(startNewBattleResponse);
                clientHandler2.out.println(responseStr);
                clientHandler2.out.flush();
                continue;
            }
            if (request instanceof BattleActionRequest){
                BattleActionResponse battleActionResponse = new BattleActionResponse((BattleActionRequest) request);
                ClientHandler clientHandler = Server.clientHandlers.get(((BattleActionRequest)request).getOpponentUserName());
                responseStr = gson.toJson(battleActionResponse);
                clientHandler.out.println(responseStr);
                clientHandler.out.flush();
                continue;
            }
        }
    }

    private void showOnlineAccounts() {
        System.out.print("\u001B[1000m" + "" + "\u001B[1000m");//resetting color
        System.out.println("Online Accounts:");
        Account.getAccountsMapper().values().forEach(account ->
                System.out.println(account.getUserName()));
    }

    private void broadcastMessage(String receiveMessageResponseStr) {
        for (ClientHandler clientHandler : Server.clientHandlers.values()){
            clientHandler.getOut().println(receiveMessageResponseStr);
            clientHandler.getOut().flush();
        }
    }
}
