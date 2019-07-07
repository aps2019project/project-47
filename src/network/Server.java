package network;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Message;
import models.Shop;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Application {

    public static Shop shop = Shop.getInstance();

    public static Shop getShop() {
        return shop;
    }

    public static ArrayList<Message> messages = new ArrayList<>();

    public static HashMap<String, Integer> userLastMessageReceivedIndex = new HashMap<>();

    public static HashMap<String, ClientHandler> clientHandlers = new HashMap<>();

    public static void increamentMessageIndex(String authToken){
        Server.userLastMessageReceivedIndex.put(authToken, Server.userLastMessageReceivedIndex.get(authToken) + 1);
    }

    public static HashMap<Integer, Integer> battleConections = new HashMap<>();

    public static void runServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        while (true) {
            try {
                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted");
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.setName("cHandler");
                clientHandler.start();
            } catch (IOException ignored) {
            }
        }
    }


    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("src/network/config"));
                int port = Integer.parseInt(reader.readLine());
                reader.close();
                runServer(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../layouts/serverGraphics.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Shop Inventory");
        stage.setScene(scene);
        stage.show();
    }
}