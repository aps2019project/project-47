package network.Responses.battle;

import controllers.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import network.Client;
import network.Requests.battle.NewBattleRequest;
import network.Responses.Response;

import java.io.IOException;

public class AcceptancePageResponse extends Response {
    public AcceptancePageResponse(NewBattleRequest newBattleRequest){
        this.request = newBattleRequest;
    }

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/layouts/newBattleReq.fxml"));
            Client.getStage().getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

}
