package network;

import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) {
        //File configFile = new File(())
//        new
    }

}