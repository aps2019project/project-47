package network.Responses.battle;

import controllers.Constants;
import controllers.graphical.BattleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.Account;
import models.battle.Battle;
import models.battle.MatchType;
import models.battle.Player;
import models.battle.board.Board;
import network.Client;
import network.Requests.battle.StartNewBattleRequest;
import network.Responses.Response;
import network.Server;

import java.io.IOException;

public class StartNewBattleResponse extends Response {
    public StartNewBattleResponse(StartNewBattleRequest request){
        this.request = request;
    }
    Player player1;
    Player player2;
    MatchType matchType;
    int numberOfFlags;
    @Override
    public void handleRequest() {
        Account account1 = null;
        for (String authToken : Account.getAccountsMapper().keySet()){
            if (((StartNewBattleRequest) request).getUserNameOfOpponent().equals(Account.getAccountsMapper().get(authToken).getUserName())){
                account1 = Account.getAccountsMapper().get(authToken);
                break;
            }
        }
        Account account2 = Account.getAccountsMapper().get(request.getAuthToken());
        //todo playerNum chiye daghighan
        player1 = account1.makePlayer(0);
        player2 = account2.makePlayer(1);
        matchType = ((StartNewBattleRequest) request).getMatchType();
        numberOfFlags = ((StartNewBattleRequest) request).getNumberOfFlags();
    }

    @Override
    public void handleResponse() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/layouts/battlePlane.fxml"));
            Client.getStage().getScene().setRoot(root);
            Battle battle = new Battle(player1, player2, matchType, numberOfFlags);
            BattleController controller = (BattleController) Board.getController();
            controller.initializeBattle(battle, true, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
