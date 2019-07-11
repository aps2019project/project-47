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
    public StartNewBattleResponse(StartNewBattleRequest request) {
        this.request = request;
    }

    Player player1;
    Player player2;
    MatchType matchType;
    int numberOfFlags;
    Battle battle;
    private boolean whoIsUnHuman;

    @Override
    public void handleRequest() {
        Account account1 = null;
        for (String authToken : Account.getAccountsMapper().keySet()) {
            if (((StartNewBattleRequest) request).getUserNameOfOpponent().equals(Account.getAccountsMapper().get(authToken).getUserName())) {
                account1 = Account.getAccountsMapper().get(authToken);
                break;
            }
        }
        Account account2 = Account.getAccountsMapper().get(request.getAuthToken());
        //todo playerNum chiye daghighan
        player1 = account1.makePlayer(0); // hamoon ke avale kar requeste start battle dade
        player2 = account2.makePlayer(1);
        matchType = ((StartNewBattleRequest) request).getMatchType();
        numberOfFlags = ((StartNewBattleRequest) request).getNumberOfFlags();
        battle = new Battle(player1, player2, matchType, numberOfFlags);

    }

    @Override
    public void handleResponse() {
        Parent root = Board.getRoot();
        BattleController controller = (BattleController) Board.getController();
        controller.initializeBattle(battle, true, false);
        Client.getStage().getScene().setRoot(root);
        if (whoIsUnHuman) {//vase nafare dovom
            battle.getPlayers()[0].setHuman(false);
            battle.getPlayers()[1].setHuman(true);
        } else {//vase nafare aval
            battle.getPlayers()[1].setHuman(false);
            battle.getPlayers()[0].setHuman(true);
        }
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }

    public boolean getWhoIsUnHuman() {
        return whoIsUnHuman;
    }

    public void setWhoIsUnHuman(boolean whoIsUnHuman) {
        this.whoIsUnHuman = whoIsUnHuman;
    }
}
