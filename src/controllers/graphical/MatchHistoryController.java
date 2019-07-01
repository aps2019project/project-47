package controllers.graphical;

import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Account;
import models.battle.MatchResult;
import runners.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MatchHistoryController implements Initializable {
    Account loginAccount = AccountMenu.getLoginAccount();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<MatchResult> matchResults = loginAccount.getMatchHistory();
        for (MatchResult matchResult : matchResults){
            board.getChildren().add(monoMatchResultLabel(matchResult.getUser0(), matchResult.getUser1(), matchResult.getWinner(), matchResult.getGameTime()));
        }
    }

    @FXML
    private VBox board;

    @FXML
    private Button backButton;

    @FXML
    void back(ActionEvent event) {
        Main.getStage().getScene().setRoot(MainMenu.getRoot());
    }

    public Label monoMatchResultLabel(String player0, String player1, int winner, Integer turns) {
        Label label = null;
        if (winner == 0)
            label = new Label(player0 + " VS " + player1 + "   winner: " + player0 + " turns: " + turns);
        else if(winner == 1)
            label = new Label(player0 + " VS " + player1 + "   winner: " + player1 + " turns: " + turns);
        label.setStyle("-fx-font-size: 19; -fx-font-weight: 900");
        return label;
    }
}
