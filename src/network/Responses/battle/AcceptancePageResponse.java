package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.NewBattleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.Account;
import network.Client;
import network.Requests.battle.NewBattleRequest;
import network.Responses.Response;

import java.io.IOException;

public class AcceptancePageResponse extends Response {
    public AcceptancePageResponse(NewBattleRequest newBattleRequest) {
        this.request = newBattleRequest;
    }

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/layouts/newBattleReq.fxml"));
            Client.getStage().getScene().setRoot(root);
            NewBattleController.instance.setText(((NewBattleRequest) request).getOpponentUserName());
            NewBattleController.instance.setUserNameOfRequest(Account.
                    getAccountsMapper().
                    get(request.getAuthToken())
                    .getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

}
