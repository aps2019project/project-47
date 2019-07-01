package network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


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
        BufferedReader reader = new BufferedReader(new FileReader("network/config"));
        int port = Integer.parseInt(reader.readLine());
        reader.close();
        new Server(port);
    }

}