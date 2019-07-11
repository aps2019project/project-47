package controllers.graphical;

import com.gilecode.yagson.YaGson;
import controllers.MyController;
import controllers.console.AccountMenu;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import layouts.AlertHelper;
import models.Account;
import models.Shop;
import models.battle.Battle;
import models.battle.StoryGame;
import models.battle.board.Board;
import network.Client;
import network.Requests.account.LogoutRequest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.ResourceBundle;

public class MainMenuController extends MyController{

    public Account loginAccount = AccountMenu.getLoginAccount();

    public static YaGson yaGson;

    public static MainMenuController instance;
    public AnchorPane anchorPane;

    {
        instance = this;
    }

    public void goToPlayMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../../layouts/battleChooseMenu.fxml"));
            Client.getStage().getScene().setRoot(root);
            BattleChooseMenuController.instance.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToCollectionMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/layouts/Collection.fxml"));
        Client.setMusic();
        Client.getStage().getScene().setRoot(root);
    }

    public void goToShop() {
        Client.getStage().getScene().setRoot(Shop.getRoot());
        try {
            UniversalShopController.instance.buyID("000");
            UniversalShopController.instance.topContainer.getChildren().remove(0, UniversalShopController.instance.topContainer.getChildren().size());
            UniversalShopController.instance.bottomContainer.getChildren().remove(0, UniversalShopController.instance.bottomContainer.getChildren().size());
        } catch (NullPointerException ignored) {
        }
    }

    public void goToHistoryMenu() {
        try {
            Client.getStage().getScene().setRoot(FXMLLoader.load(getClass().getResource("/layouts/MatchHistory.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut() {
        yaGson = new YaGson();
        LogoutRequest request = new LogoutRequest(AccountMenu.getLoginAccount().getAuthToken());
        Client.getWriter().println(yaGson.toJson(request));
        Client.getWriter().flush();
    }

    public void doLogOut() {
        AccountMenu.setLoginAccount(null);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/layouts/accountPage.fxml"));
            Client.getStage().getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToCustomCardMenu() throws IOException {
        Client.getStage().getScene().setRoot(FXMLLoader.load(getClass().getResource("../../layouts/customCardCreatePage.fxml")));
    }

    public void exit() {
        logOut();
        System.exit(0);
    }

    public void saveAccount() {
        if (loginAccount == null)
            return;
        yaGson = new YaGson();
        String json = yaGson.toJson(loginAccount);
        try {
            Path path = Paths.get("src/JSONs/Accounts/" + loginAccount.getUserName() + ".json");
            FileOutputStream out = new FileOutputStream(path.toString());
            Formatter formatter = new Formatter(out);
            formatter.format(json);
            formatter.flush();
            formatter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, Client.getStage().getOwner(), "Account Saved!", "Account " + loginAccount.getUserName() + " Saved!");
    }

    public void goToGlobalChatForm() throws IOException {
        Client.getStage().getScene().setRoot(FXMLLoader.load(getClass().getResource("../../layouts/globalChatForm.fxml")));
    }

    public void goToScoreBoard() throws IOException {
        Client.getStage().getScene().setRoot(FXMLLoader.load(getClass().getResource("../../layouts/scoreBoard.fxml")));
    }

    public void learnGame(MouseEvent mouseEvent) {
        StoryGame storyGame = new StoryGame();
        Battle battle = storyGame.getRandomBattle();
        Parent root = Board.getRoot();
        BattleController controller = (BattleController) Board.getController();
        controller.initializeBattle(battle, false, false);
        controller.setOnStoryMod();
        Client.getStage().getScene().setRoot(root);
    }
}
