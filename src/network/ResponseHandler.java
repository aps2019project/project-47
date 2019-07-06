package network;

import com.gilecode.yagson.YaGson;
import controllers.console.AccountMenu;
import controllers.graphical.LoginRegisterController;
import javafx.animation.AnimationTimer;
import network.Responses.CreateAccountResponse;
import network.Responses.LoginResponse;
import network.Responses.ReceiveMessageResponse;
import network.Responses.Response;

import java.util.Scanner;

import static network.ReqResType.*;

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

                    switch (currentResponse.getType()){
                        case accountMenu:
                                handleAccountMenuRes(currentResponse);
                            break;
                        case shop:
                            break;
                        case battle:
                            break;
                        case chatMenu:
                            break;
                    }
                }
            }
        };
    }

    private void handleAccountMenuRes(Response currentResponse) {
        if (currentResponse instanceof CreateAccountResponse){
            if (AccountMenu.getController()!=null){
                ((LoginRegisterController) AccountMenu.getController()).createAccount(currentResponse.getRequestResult());
            }
        }
        if (currentResponse instanceof LoginResponse){
            if(AccountMenu.getController() != null){
                ((LoginRegisterController) AccountMenu.getController().)
            }
        }
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
