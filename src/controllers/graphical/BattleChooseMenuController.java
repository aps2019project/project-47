package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import controllers.MyController;
import controllers.console.AccountMenu;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Account;

public class BattleChooseMenuController extends MyController {
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

    }

    public void startMultiPlayerGame(ActionEvent event) {

    }
    @Override
    public void update(){
        loginAccount=AccountMenu.getLoginAccount();
        storyGame();
    }
    private void storyGame(){
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
}
