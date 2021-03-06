package controllers.graphical;

import com.gilecode.yagson.YaGson;
import controllers.console.AccountMenu;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.Account;
import models.battle.MatchType;
import network.Client;
import network.Requests.battle.CancelNewBattleRequest;
import network.Requests.battle.RejectNewGameRequest;
import network.Requests.battle.StartNewBattleRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewBattleController implements Initializable {

    public static NewBattleController instance;

    {
        instance = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alarmer.start();
    }

    @FXML
    private AnchorPane container;

    @FXML
    private Button rejectButton;

    @FXML
    private Button acceptButton;

    @FXML
    private Label text;

    private Account loginAccount = AccountMenu.getLoginAccount();

    private YaGson yaGson = new YaGson();

    private String userNameOfRequest;
    private MatchType matchType;
    private int numberOfFlags;
    private double turnTime;
    public String getUserNameOfRequest() {
        return userNameOfRequest;
    }

    public double getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(double turnTime) {
        this.turnTime = turnTime;
    }

    public void setUserNameOfRequest(String userNameOfRequest) {
        this.userNameOfRequest = userNameOfRequest;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public void setNumberOfFlags(int numberOfFlags) {
        this.numberOfFlags = numberOfFlags;
    }

    public void setText(){
        text.setText("Player " + userNameOfRequest + " wants to start new battle with you");
    }

    AnimationTimer alarmer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            animation.play();
        }
    };
    final Animation animation = new Transition() {

        {
            setCycleDuration(Duration.millis(6000));
            setInterpolator(Interpolator.EASE_OUT);
        }
        boolean redo = true;
        @Override
        protected void interpolate(double frac) {
            if ((redo && frac > 0.5) || ( !redo && frac < 0.5))
                redo = !redo;
            if (redo) {
                String gradient = "linear-gradient(to right, rgb(26,34,46," + (1 - frac) + "), rgb(54,96,111,"+2*(0.5 - frac) + "))";
                Color vColor = new Color(0, 0, 0, 2*(0.5 - frac));
                container.setStyle("-fx-background-color: " + gradient);
            }
            else {
                String gradient = "linear-gradient(to right, rgb(26,34,46," + (frac) + "), rgb(54,96,111,"+2 * (frac - 0.5) + "))";
                Color vColor = new Color(0, 0, 0,  2*(frac - 0.5));
                container.setStyle("-fx-background-color: " + gradient);
            }
        }
    };

    @FXML
    private void accept(){
        if (!loginAccount.checkCorrectyDeck()){
            reject();
            return;
        }
        StartNewBattleRequest startNewBattleRequest = new StartNewBattleRequest(loginAccount.getAuthToken(), userNameOfRequest, matchType, numberOfFlags, turnTime);
        Client.getWriter().println(yaGson.toJson(startNewBattleRequest));
        Client.getWriter().flush();
    }

    @FXML
    private void reject(){
        RejectNewGameRequest rejectNewGameRequest = new RejectNewGameRequest(loginAccount.getAuthToken(), userNameOfRequest);
        Client.getWriter().println(yaGson.toJson(rejectNewGameRequest));
        Client.getWriter().flush();
    }
}
