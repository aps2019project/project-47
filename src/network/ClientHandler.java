package network;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import models.Shop;
import network.Requests.*;
import network.Responses.BuyResponse;
import network.Responses.CreateAccountResponse;
import network.Responses.FindResponse;
import network.Responses.LoginResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    public static YaGsonBuilder gsonBuilder = new YaGsonBuilder();
    public static YaGson gson = gsonBuilder.create();
    private static Server server;
    private PrintWriter out;
    private Scanner scanner;

    public static void setServer(Server server) {
        ClientHandler.server = server;
    }

    public static Server getServer() {
        return server;
    }

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
        for(int i = 0; i < 100; i++){
            System.out.println(i);
        }
        while (scanner.hasNextLine()) {
            String str = this.scanner.nextLine();
            Request request = gson.fromJson(str, Request.class);
            String responseStr = "";
            if (request instanceof BuyRequest) {
                BuyResponse buyResponse = new BuyResponse();
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
                if (Server.getTokens().get(request.getAuthToken()) != null)
                    Shop.getInstance().command_sell(((SellRequest) request).getCode());
                continue;
            }
            if (request instanceof CreateAccountRequest){
                CreateAccountResponse createAccountResponse = new CreateAccountResponse((CreateAccountRequest) request);
                createAccountResponse.handleRequest();
                responseStr = gson.toJson(createAccountResponse);
                out.println(responseStr);
                out.flush();
                continue;
            }
            if (request instanceof LoginRequest){
                LoginResponse loginResponse = new LoginResponse((LoginRequest) request);
                loginResponse.handleRequest();
                responseStr = gson.toJson(loginResponse);
                out.println(responseStr);
                out.flush();
                continue;
            }
        }
    }
}
