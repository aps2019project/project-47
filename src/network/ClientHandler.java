package network;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import network.Requests.BuyRequest;
import network.Requests.Request;
import network.Responses.BuyResponse;

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
        while (true) {
            String str = this.getScanner().nextLine();
            Request request = gson.fromJson(str, Request.class);
            if (request instanceof BuyRequest) {
                BuyResponse buyResponse = new BuyResponse();
                buyResponse.handleRequest(request);
                String responseStr = gson.toJson(buyResponse);
                out.println(responseStr);
                out.flush();
            }

        }
    }
}
