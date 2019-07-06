package network;

import com.gilecode.yagson.YaGson;
import javafx.animation.AnimationTimer;
import network.Responses.ReceiveMessageResponse;
import network.Responses.Response;

import java.util.Scanner;

public class ResponseHandler {
    private String currentResponseStr;
    private Response currentResponse;
    private Object lock = new Object();

    private ResponseHandler() {
        Scanner responseScanner = Client.getServerScanner();
        YaGson yaGson = new YaGson();
        AnimationTimer listener = new AnimationTimer() {
            @Override
            public void handle(long now) {
                while (responseScanner.hasNextLine()) {
                    currentResponseStr = responseScanner.nextLine();
                    currentResponse = yaGson.fromJson(currentResponseStr, Response.class);

                    if (currentResponse)
                }
            }
        };
    }

    private static ResponseHandler responseHandler;

    public static ResponseHandler getInstance() {
        if (responseHandler == null)
            responseHandler = new ResponseHandler();
        return responseHandler;
    }

    public String getCurrentResponseStr() {
        return currentResponseStr;
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
