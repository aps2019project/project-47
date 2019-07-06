package network;

import com.gilecode.yagson.YaGson;
import network.Responses.ReceiveMessageResponse;
import network.Responses.Response;

import java.util.Scanner;

public class ResponseHandler {
    private String currentResponseStr;
    private Response currentResponse;
    private Object lock = new Object();
    private ResponseHandler(){
        Scanner responseScanner = Client.getServerScanner();
        YaGson yaGson = new YaGson();
        Thread listener = new Thread(() -> {
            while (responseScanner.hasNextLine()) {
                synchronized (lock) {
                    currentResponseStr = responseScanner.nextLine();
                    currentResponse = yaGson.fromJson(currentResponseStr, Response.class);

                    if (currentResponse instanceof ReceiveMessageResponse){

                    }
                }
            }
        });
        listener.start();
    }

    private static ResponseHandler responseHandler;
    public static ResponseHandler getInstance(){
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

    public void clearResponse(){
        synchronized (lock) {
            currentResponse = null;
            currentResponseStr = null;
        }
    }
    public void handleAttackResponse(){
        
    }
}
