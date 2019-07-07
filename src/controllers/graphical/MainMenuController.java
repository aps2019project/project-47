package controllers.graphical;

import com.gilecode.yagson.YaGson;
import controllers.MyController;
import controllers.console.AccountMenu;
import controllers.console.BattleMenu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import layouts.AlertHelper;
import models.Account;
import models.Shop;
import network.Client;
import network.Requests.account.LogoutRequest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;

public class MainMenuController extends MyController {

    public Account loginAccount = AccountMenu.getLoginAccount();

    public static YaGson yaGson;

    public static MainMenuController instance;
    {
        instance = this;
    }

    public void goToPlayMenu() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../../layouts/battleChooseMenu.fxml"));
        } catch (IOException ignored) {
        }
        Client.getStage().getScene().setRoot(root);
    }

    public void goToCollectionMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/layouts/Collection.fxml"));
        Client.getStage().getScene().setRoot(root);
    }

    public void goToShop() {
        Client.getStage().getScene().setRoot(Shop.getRoot());
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

    public void doLogOut(){
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

    public void goToScoreBoard(MouseEvent mouseEvent) {
    }
}
