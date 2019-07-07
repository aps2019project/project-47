package controllers.graphical;

import com.gilecode.yagson.YaGson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import controllers.MyController;
import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Account;
import models.battle.Battle;
import models.battle.BattleHistory;
import models.battle.Player;
import models.battle.StoryGame;
import models.battle.board.Board;
import network.Client;
import network.Requests.battle.CancelNewBattleRequest;
import network.Requests.battle.NewBattleRequest;
import network.Requests.battle.OnlinePlayersRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class BattleChooseMenuController extends MyController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setDisable(true);
    }

    public static BattleChooseMenuController instance;

    {
        instance = this;
    }

    public JFXComboBox mode;
    public JFXComboBox otherPlayers;
    public JFXButton btn_back;
    private Account loginAccount = AccountMenu.getLoginAccount();

    public JFXTabPane mainPage;
    public VBox SingleGameTab;
    public JFXTextField numberOfFlags;
    public JFXButton startSinglePlayer;
    public VBox storyGameTab;
    public Label levelOfStoryGame;
    public JFXButton startStoryGame;
    public VBox multiPlayerGameTab;
    public JFXButton startMultiPlayerGame;
    @FXML
    public JFXButton cancelButton;


    public static YaGson yaGson = new YaGson();

    public void startSinglePlayer(ActionEvent event) {

    }

    public void startStoryGame(ActionEvent event) {

        Account account = AccountMenu.getLoginAccount();
        Player player0 = account.makePlayer(0);
        StoryGame storyGame = new StoryGame();
        Battle battle = storyGame.story(player0, account.getStoryLvl());
        Parent root = Board.getRoot();
        BattleController controller = (BattleController) Board.getController();
        controller.initializeBattle(battle, false, false);
        Client.getStage().getScene().setRoot(root);
    }

    public void startMultiPlayerGame(ActionEvent event) {
        if (otherPlayers.getSelectionModel().getSelectedItem() == null)
            return;
        String opponentUserName = (String) (otherPlayers.getSelectionModel().getSelectedItem());
        NewBattleRequest newBattleRequest = new NewBattleRequest(loginAccount.getAuthToken(), opponentUserName);
        Client.getWriter().println(yaGson.toJson(newBattleRequest));
        Client.getWriter().flush();
        cancelButton.setDisable(false);
        disableEveryThing();
    }

    private void disableEveryThing() {
        otherPlayers.setDisable(true);
        btn_back.setDisable(true);
        mainPage.setDisable(true);
        startMultiPlayerGame.setDisable(true);
    }

    private void enableEveryThing() {
        otherPlayers.setDisable(false);
        btn_back.setDisable(false);
        mainPage.setDisable(false);
        startMultiPlayerGame.setDisable(false);
    }

    @Override
    public void update() {
        setStoryGame();
        setSingleGame();
    }

    public void setSingleGame() {
        mode.getItems().removeAll(mode.getItems());
        mode.getItems().addAll("KILL", "COLLECTING", "KEEPING");
    }

    public void setMultiPlayerGame() {
        otherPlayers.getItems().removeAll(otherPlayers.getItems());
        for (Account account : AccountMenu.getAccounts()) {
            otherPlayers.getItems().add(account.getUserName());
        }
    }

    private void setStoryGame() {
        int lvl = loginAccount.getStoryLvl();
        if (lvl == 4) {
            storyGameTab.getChildren().remove(startStoryGame);
            levelOfStoryGame.setText("You have finished story game!");
            return;
        }
        String number = "";
        switch (lvl) {
            case 1: {
                number = "one";
                break;
            }
            case 2: {
                number = "two";
                break;
            }
            case 3: {
                number = "three";
                break;
            }
        }
        levelOfStoryGame.setText("Are you sure to start mission " + number + " ?");
    }

    public void back(ActionEvent event) {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }

    public void getOnlinePlayers() {
        otherPlayers.getItems().remove(0, otherPlayers.getItems().size());
        OnlinePlayersRequest onlinePlayersRequest = new OnlinePlayersRequest(loginAccount.getAuthToken());
        Client.getWriter().println(yaGson.toJson(onlinePlayersRequest));
        Client.getWriter().flush();
    }

    public void addOtherPlayersUserNames(ArrayList<String> userNames) {
        for (String userName : userNames) {
            otherPlayers.getItems().add(userName);
        }
    }

    @FXML
    private void cancelRequest(ActionEvent actionEvent) {
        doCancel();
    }

    public void doCancel() {
        cancelButton.setDisable(true);
        enableEveryThing();
        CancelNewBattleRequest cancelNewBattleRequest = new CancelNewBattleRequest(loginAccount.getAuthToken(), (String) otherPlayers.getSelectionModel().getSelectedItem());
        Client.getWriter().println(yaGson.toJson(cancelNewBattleRequest));
        Client.getWriter().flush();
    }
    public void reView(){
        YaGson yaGson = new YaGson();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream("fileName.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String scanned = scanner.nextLine();
        System.out.println(scanned);
        BattleHistory battleHistory = yaGson.fromJson(scanned,BattleHistory.class);
        Battle battle =(battleHistory.getBattel());
        Parent root=Board.getRoot();
        System.out.println(battleHistory.battleActions.size());
        BattleController controller = (BattleController) Board.getController();
        controller.initializeBattle(battle,false,true);
        controller.setHistory(battleHistory);
        Client.getStage().getScene().setRoot(root);
    }
}
