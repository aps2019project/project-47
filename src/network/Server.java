package network;

import models.Account;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    public static HashMap<String , Account> tokens = new HashMap<>();

    public static HashMap<String, Account> getTokens() {
        return tokens;
    }

    public Server(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server started");
        ClientHandler.setServer(this);
        while (true) {
            try {
                System.out.println("Waiting for a client ...");
                Socket socket = server.accept();
                System.out.println("Client accepted");
                ClientHandler clientHandler = new ClientHandler(socket);
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