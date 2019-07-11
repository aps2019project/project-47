package controllers.graphical;

import com.gilecode.yagson.YaGson;
import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Account;
import network.Client;
import network.Requests.account.ScoreBoardRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreBoardController implements Initializable {
    private int numberOfRows = 1;
    @FXML
    private GridPane gridPane;
    private Account loginAccount = AccountMenu.getLoginAccount();
    private YaGson yaGson = new YaGson();
    public static ScoreBoardController instance;
    public static boolean loaded;

    public GridPane getGridPane() {
        return gridPane;
    }

    {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loaded = true;
        ScoreBoardRequest scoreBoardRequest = new ScoreBoardRequest(loginAccount.getAuthToken());
        Client.getWriter().println(yaGson.toJson(scoreBoardRequest));
        Client.getWriter().flush();
    }

    public void back(MouseEvent mouseEvent) {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }

    public void addDetails(Account account, int row) {
        numberOfRows++;
        gridPane.add(new Label(account.getUserName()), 0, row);
        gridPane.add(new Label(Integer.toString(account.getWins())), 1, row);
        gridPane.add(new Label(Integer.toString(account.getLoses())), 2, row);
        if (account.isOnline()){
            gridPane.add(new Label("Online"), 3, row);
        }
        else
            gridPane.add(new Label("Offline"), 3, row);
    }

    public void clearGridPane() {
        if (gridPane.getChildren().size() >= 4)
            gridPane.getChildren().remove(4, gridPane.getChildren().size());
    }
}
