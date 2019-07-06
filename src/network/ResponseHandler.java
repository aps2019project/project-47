package network;

import com.gilecode.yagson.YaGson;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import network.Responses.Response;

import java.util.Scanner;

public class ResponseHandler {
    //    private final AnimationTimer listener;
    private Thread listener;
    private String currentResponseStr;
    private Response currentResponse;
    private Object lock = new Object();

    private ResponseHandler() {
        Scanner responseScanner = Client.getServerScanner();
        YaGson yaGson = new YaGson();

//        listener = new AnimationTimer() {
//            int lastTime = 0;
//            @Override
//            public void handle(long now) {
//
//                if(responseScanner.hasNextLine()) {
//                    currentResponseStr = responseScanner.nextLine();
//                    currentResponse = yaGson.fromJson(currentResponseStr, Response.class);
//                    currentResponse.handleResponse();
//                }
//                lastTime++;
//                System.out.println(lastTime);
//                if (lastTime>100){
//                    stop();
//                }
//            }
//        };
        listener = new Thread() {
            @Override
            public void run() {
                while (responseScanner.hasNextLine()) {
                    currentResponseStr = responseScanner.nextLine();
                    currentResponse = yaGson.fromJson(currentResponseStr, Response.class);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            currentResponse.handleResponse();
                        }
                    });
                }
            }
        };
        listener.setPriority(1);

    }

    public void start() {
        listener.start();
        System.out.println("k1");
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
