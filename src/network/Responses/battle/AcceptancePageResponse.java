package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.NewBattleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.Account;
import models.battle.MatchType;
import network.Client;
import network.Requests.battle.NewBattleRequest;
import network.Responses.Response;

import java.io.IOException;

public class AcceptancePageResponse extends Response {
    private String requesterUserName;
    private MatchType matchType;
    private int numberOfFlags;
    public AcceptancePageResponse(NewBattleRequest newBattleRequest) {
        this.request = newBattleRequest;
    }

    @Override
    public void handleRequest() {
        requesterUserName = Account.getAccountsMapper().get(request.getAuthToken()).getUserName();
        matchType = ((NewBattleRequest) request).getMatchType();
        numberOfFlags = ((NewBattleRequest) request).getNumOfFlags();
    }

    @Override
    public void handleResponse() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/layouts/newBattleReq.fxml"));
            Client.getStage().getScene().setRoot(root);
            NewBattleController.instance.setUserNameOfRequest(requesterUserName);
            NewBattleController.instance.setText();
            NewBattleController.instance.setMatchType(this.matchType);
            NewBattleController.instance.setNumberOfFlags(this.numberOfFlags);
            NewBattleController.instance.setTurnTime(((NewBattleRequest) request).getTurnTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

}
