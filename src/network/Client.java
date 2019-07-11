package network;

import controllers.console.AccountMenu;
import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import static network.Server.getPort;

public class Client extends Application {

    private static Stage stage;
    private static PrintWriter writer;
    private static Scanner serverScanner;
    private static MediaPlayer music;

    public static PrintWriter getWriter() {
        return writer;
    }

    public static Scanner getServerScanner() {
        return serverScanner;
    }

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void init() {
        try {
            int port = getPort();
            String ip = getIP();
            Socket socket = new Socket(ip, port);
            writer = new PrintWriter(socket.getOutputStream());
            DataInputStream serverResponse = new DataInputStream(socket.getInputStream());
            serverScanner = new Scanner(serverResponse);
            ResponseHandler.getInstance().start();
        } catch (Exception e) {
            System.err.println("Server doesn't  run yet!");
            System.exit(0);
        }
    }

    public static String getIP() throws IOException {
        FileReader fileReader = new FileReader("src/network/Config.properties");
        Properties properties = new Properties();
        properties.load(fileReader);
        String ip = properties.getProperty("IP");
        fileReader.close();
        return ip;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Parent root = AccountMenu.getRoot();
//        Parent root = FXMLLoader.load(getClass().getResource("/layouts/accountPage.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/layouts/newBattleReq.fxml"));
//        Parent root = MainMenu.getRoot();
//        Parent root = Shop.getRoot();
//        Parent root = BattleMenu.getRoot();
//        Parent root = Board.getRoot();
//        Parent root = FXMLLoader.load(getClass().getResource("../layouts/customCardCreatePage.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("../layouts/Collection.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("../layouts/globalChatForm.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setMaximized(true);
        stage.show();
        System.out.println("\u001B[1000m" + "" + "\u001B[1000m");//resetting color
        setCursor();
    }

    public void setCursor() {
        Image img = new Image("/resources/buttons/cursor.png");
        ImageCursor cursor = new ImageCursor(img, 0, 0);
        stage.getScene().setCursor(cursor);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setMusic() {
        Random random = new Random();
        String address = "src/resources/music/battle/" + random.nextInt(20) + ".m4a";
        Media media = new Media(new File(address).toURI().toString());
        stpoMusic();
        music = new MediaPlayer(media);
        music.setVolume(0.2);
        music.play();
    }
    public static void stpoMusic(){
        if (music != null) {
            music.stop();
        }
    }
}