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
    private Object lock = new Object();

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


    public Response getCurrentResponse() {
        return currentResponse;
    }

    public void clearResponse() {
        synchronized (lock) {
            currentResponse = null;
            currentResponseStr = null;
        }
    }

    public static void waitForResponse() {
        while (ResponseHandler.getInstance().getCurrentResponse() == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
