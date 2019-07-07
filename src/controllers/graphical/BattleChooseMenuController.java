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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Account;
import models.battle.Battle;
import models.battle.Player;
import models.battle.StoryGame;
import models.battle.board.Board;
import network.Client;
import network.Requests.battle.NewBattleRequest;
import network.Requests.battle.OnlinePlayersRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BattleChooseMenuController extends MyController{

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
    public JFXButton StartMultiPlayerGame;


    public static YaGson yaGson = new YaGson();
    public void startSinglePlayer(ActionEvent event) {

    }

    public void startStoryGame(ActionEvent event) {

        Account account = AccountMenu.getLoginAccount();
        Player player0 = account.makePlayer(0);
        StoryGame storyGame = new StoryGame();
        Battle battle = storyGame.story(player0, account.getStoryLvl());
        Parent root=Board.getRoot();
        BattleController controller = (BattleController) Board.getController();
        controller.initializeBattle(battle,false,false);
        Client.getStage().getScene().setRoot(root);
    }

    public void startMultiPlayerGame(ActionEvent event) {
        String opponentUserName = (String)(otherPlayers.getSelectionModel().getSelectedItem());
        NewBattleRequest newBattleRequest = new NewBattleRequest(loginAccount.getAuthToken(), opponentUserName);
        Client.getWriter().println(yaGson.toJson(newBattleRequest));
        Client.getWriter().flush();
    }
    @Override
    public void update(){
        loginAccount=AccountMenu.getLoginAccount();
        setStoryGame();
        setSingleGame();
    }

    public void setSingleGame(){
        mode.getItems().removeAll(mode.getItems());
        mode.getItems().addAll("KILL","COLLECTING","KEEPING");
    }
    public void setMultiPlayerGame(){
        otherPlayers.getItems().removeAll(otherPlayers.getItems());
        for (Account account:AccountMenu.getAccounts()){
            otherPlayers.getItems().add(account.getUserName());
        }
    }
    private void setStoryGame(){
        int lvl=loginAccount.getStoryLvl();
        if (lvl==4){
            storyGameTab.getChildren().remove(startStoryGame);
            levelOfStoryGame.setText("You have finished story game!");
            return;
        }
        String number="";
        switch (lvl){
            case 1:{
                number="one";
                break;
            }
            case 2:{
                number="two";
                break;
            }
            case 3:{
                number="three";
                break;
            }
        }
        levelOfStoryGame.setText("Are you sure to start mission "+number+" ?");
    }

    public void setLoginAccount(Account loginAccount) {
        this.loginAccount = loginAccount;
    }

    public void back(ActionEvent event) {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }

    public void getOnlinePlayers(){
        otherPlayers.getItems().remove(0, otherPlayers.getItems().size());
        OnlinePlayersRequest onlinePlayersRequest = new OnlinePlayersRequest(
                loginAccount.
                        getAuthToken());
        Client.getWriter().println(yaGson.toJson(onlinePlayersRequest));
        Client.getWriter().flush();
    }

    public void addOtherPlayersUserNames(ArrayList<String> userNames){
        for (String userName : userNames){
            otherPlayers.getItems().add(userName);
        }
    }
}
