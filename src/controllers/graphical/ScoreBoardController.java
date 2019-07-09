package controllers.graphical;

import com.gilecode.yagson.YaGson;
import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Account;
import network.Client;
import network.Requests.account.ScoreBoardRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreBoardController implements Initializable {
    @FXML
    private GridPane gridPane;
    private Account loginAccount = AccountMenu.getLoginAccount();
    private YaGson yaGson = new YaGson();
    public static ScoreBoardController instance;

    {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ScoreBoardRequest scoreBoardRequest = new ScoreBoardRequest(loginAccount.getAuthToken());
        Client.getWriter().println(yaGson.toJson(scoreBoardRequest));
        Client.getWriter().flush();
    }

    public void back(MouseEvent mouseEvent) {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }
}
