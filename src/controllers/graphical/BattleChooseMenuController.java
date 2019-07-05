package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import controllers.MyController;
import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;

public class BattleChooseMenuController extends MyController {
    public ComboBox mode;
    public ComboBox otherPlayers;
    public JFXButton btn_back;
    private Account loginAccount;

    public JFXTabPane mainPage;
    public VBox SingleGameTab;
    public JFXTextField numberOfFlags;
    public JFXButton startSinglePlayer;
    public VBox storyGameTab;
    public Label levelOfStoryGame;
    public JFXButton startStoryGame;
    public VBox multiPlayerGameTab;
    public JFXTextField opponentName;
    public JFXButton StartMultiPlayerGame;

    public void startSinglePlayer(ActionEvent event) {

    }

    public void startStoryGame(ActionEvent event) {

        Account account = AccountMenu.getLoginAccount();
        Player player0 = account.makePlayer(0);
        StoryGame storyGame = new StoryGame();
        Battle battle = storyGame.story(player0, account.getStoryLvl());
        Parent root=Board.getRoot();
        BattleController controller = (BattleController) Board.getController();
        controller.initializeBattle(battle);
        //Client.getStage().getScene().setRoot(root);
    }

    public void startMultiPlayerGame(ActionEvent event) {

    }
    @Override
    public void update(){
        loginAccount=AccountMenu.getLoginAccount();
        setStoryGame();
        setSingleGame();
        setMultiPlayerGame();

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
}
