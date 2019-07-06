package network;

import models.Message;
import models.Shop;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    private static Shop shop = Shop.getInstance();

    public static Shop getShop() {
        return shop;
    }

    public static ArrayList<Message> messages = new ArrayList<>();

    public static HashMap<String, Integer> userLastMessageReceivedIndex = new HashMap<>();

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public Server(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        ClientHandler.setServer(this);
        while (true) {
            try {
                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted");
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.setName("cHandler");
                clientHandlers.add(clientHandler);
                clientHandler.start();
            } catch (IOException ignored) {
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/network/config"));
        int port = Integer.parseInt(reader.readLine());
        reader.close();
        new Server(port);
    }

}