package network;

import com.gilecode.yagson.YaGson;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import network.Responses.Response;

import java.util.Scanner;

public class ResponseHandler {

    private Thread listener;
    private String currentResponseStr;
    private Response currentResponse;

    private ResponseHandler() {
        Scanner responseScanner = Client.getServerScanner();
        YaGson yaGson = new YaGson();
        listener = new Thread(() -> {
            while (responseScanner.hasNextLine()) {
                currentResponseStr = responseScanner.nextLine();
                currentResponse = yaGson.fromJson(currentResponseStr, Response.class);
                Platform.runLater(() -> currentResponse.handleResponse());
            }
        });
        listener.setPriority(1);

    }

    public void start() {
        listener.start();
    }

    private static ResponseHandler responseHandler;

    public static ResponseHandler getInstance() {
        if (responseHandler == null)
            responseHandler = new ResponseHandler();
        return responseHandler;
    }
}
