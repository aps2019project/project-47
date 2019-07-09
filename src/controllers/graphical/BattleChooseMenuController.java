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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import layouts.AlertHelper;
import models.Account;
import models.battle.*;
import models.battle.board.Board;
import network.Client;
import network.Requests.battle.CancelNewBattleRequest;
import network.Requests.battle.NewBattleRequest;
import network.Requests.battle.OnlinePlayersRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;

public class BattleChooseMenuController extends MyController implements Initializable {

    public static BattleChooseMenuController instance;

    {
        instance = this;
    }

    public JFXTabPane mainPage;
    public VBox storyGameTab;
    public VBox SingleGameTab;
    public VBox multiPlayerGameTab;
    public Label levelOfStoryGame;
    public JFXButton btn_back;
    public JFXButton startSinglePlayer;
    public JFXButton startStoryGame;
    public JFXButton startMultiPlayerGame;
    public JFXButton cancelButton;
    public JFXComboBox<MatchType> modeOfBattle;
    public JFXComboBox<String> decks;
    public JFXComboBox<MatchType> mode;
    public JFXComboBox<String> otherPlayers;
    public JFXTextField numOfFlags;
    public JFXTextField numberOfFlags;

    private Account loginAccount = AccountMenu.getLoginAccount();
    public static YaGson yaGson = new YaGson();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modeOfBattle.getItems().remove(0, modeOfBattle.getItems().size());
        modeOfBattle.getItems().addAll(MatchType.kill, MatchType.collectFlag, MatchType.keepFlag);
        mode.getItems().remove(0, mode.getItems().size());
        mode.getItems().addAll(MatchType.kill, MatchType.collectFlag, MatchType.keepFlag);
        cancelButton.setDisable(true);
        AccountMenu.getLoginAccount().getDecks().forEach(deck1 -> {
            if (!deck1.getName().equals(AccountMenu.getLoginAccount().getMainDeck().getName()))
                decks.getItems().add(deck1.getName());
        });
    }

    public void startSinglePlayer(ActionEvent event) {

    }

    public void startStoryGame(ActionEvent event) {
        try {
            Account account = AccountMenu.getLoginAccount();
            Player player0 = account.makePlayer(0);
            StoryGame storyGame = new StoryGame();
            Battle battle = storyGame.story(player0, account.getStoryLvl());
            Parent root = Board.getRoot();
            BattleController controller = (BattleController) Board.getController();
            controller.initializeBattle(battle, false, false);
            controller.setOnStoryMod();
            Client.getStage().getScene().setRoot(root);
        } catch (NullPointerException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, Client.getStage().getOwner(), "Deck is not complete!", "Deck is not complete!");
            AlertHelper.showAlert(Alert.AlertType.ERROR, Client.getStage().getOwner(), "Deck is not complete!", Arrays.toString(e.getStackTrace()));
        }
    }

    public void startMultiPlayerGame(ActionEvent event) {
        if (otherPlayers.getSelectionModel().getSelectedItem() == null || modeOfBattle.getSelectionModel().getSelectedItem() == null)
            return;
        String opponentUserName = (otherPlayers.getSelectionModel().getSelectedItem());
        MatchType matchType = (modeOfBattle.getSelectionModel().getSelectedItem());
        int numOfFlags = 0;
        if (!this.numberOfFlags.getText().equals(""))
            numOfFlags = Integer.valueOf(numberOfFlags.getText());
        if (matchType != MatchType.kill && numOfFlags != 0)
            return;
        NewBattleRequest newBattleRequest = new NewBattleRequest(loginAccount.getAuthToken(), opponentUserName, matchType, numOfFlags);
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
        mode.getItems().addAll(MatchType.kill, MatchType.collectFlag, MatchType.keepFlag);
    }

    public void setMultiPlayerGame() {
        otherPlayers.getItems().removeAll(otherPlayers.getItems());
        for (Account account : AccountMenu.getAccounts()) {
            otherPlayers.getItems().add(account.getUserName());
        }
    }

    public void setStoryGame() {
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
        CancelNewBattleRequest cancelNewBattleRequest = new CancelNewBattleRequest(loginAccount.getAuthToken(), otherPlayers.getSelectionModel().getSelectedItem());
        Client.getWriter().println(yaGson.toJson(cancelNewBattleRequest));
        Client.getWriter().flush();
    }

    public void reView() {
        YaGson yaGson = new YaGson();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream("fileName.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String scanned = scanner.nextLine();
        System.out.println(scanned);
        BattleHistory battleHistory = yaGson.fromJson(scanned, BattleHistory.class);
        Battle battle = (battleHistory.getBattel());
        Parent root = Board.getRoot();
        System.out.println(battleHistory.battleActions.size());
        BattleController controller = (BattleController) Board.getController();
        controller.initializeBattle(battle, false, true);
        controller.setHistory(battleHistory);
        Client.getStage().getScene().setRoot(root);
    }
}
