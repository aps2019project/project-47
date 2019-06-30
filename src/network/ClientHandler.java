package network;

import java.net.Socket;

public class ClientHandler extends Thread {
    public static Server server;
    private Socket socket;

    public static void setServer(Server server) {
        ClientHandler.server = server;
    }

    public Socket getSocket() {
        return socket;
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
