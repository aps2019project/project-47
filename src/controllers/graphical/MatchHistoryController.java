package controllers.graphical;

import com.gilecode.yagson.YaGson;
import com.jfoenix.controls.JFXButton;
import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import models.Account;
import models.battle.Battle;
import models.battle.BattleHistory;
import models.battle.MatchResult;
import models.battle.board.Board;
import network.Client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MatchHistoryController implements Initializable {
    Account loginAccount = AccountMenu.getLoginAccount();
    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<MatchResult> matchResults = loginAccount.getMatchHistory();
        int i = 1;
        for (MatchResult matchResult : matchResults){
            String userName1 = matchResult.getUser0();
            String userName2 = matchResult.getUser1();
            String winner;
            if (matchResult.getWinner() == 0)
                winner = userName1;
            else
                winner = userName2;
            JFXButton reviewButton = new JFXButton("Review");
            reviewButton.setOnMouseClicked(event -> {
                review(matchResult.getBattleHistory());
            });
            gridPane.add(new Label(userName1), 0, i);
            gridPane.add(new Label(userName2), 1, i);
            gridPane.add(new Label(winner), 2, i);
            gridPane.add(reviewButton, 3, i);
        }
    }

    public void back(MouseEvent mouseEvent) {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }

    public void review(BattleHistory battleHistory){
        Battle battle = (battleHistory.getBattel());
        Parent root = Board.getRoot();
        System.out.println(battleHistory.battleActions.size());
        BattleController controller = (BattleController) Board.getController();
        controller.initializeBattle(battle, false, true);
        controller.setHistory(battleHistory);
        Client.getStage().getScene().setRoot(root);
    }
}
