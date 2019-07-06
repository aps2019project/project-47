package controllers.graphical;

import com.gilecode.yagson.YaGson;
import controllers.console.AccountMenu;
import controllers.console.BattleMenu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import layouts.AlertHelper;
import models.Account;
import models.Shop;
import network.Client;
import network.Requests.accountMenu.LogoutRequest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;

public class MainMenuController {

    public Account loginAccount = AccountMenu.getLoginAccount();

    public static YaGson yaGson;


    public void goToPlayMenu() {
        Client.getStage().getScene().setRoot(BattleMenu.getRoot());
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
        AccountMenu.setLoginAccount(null);
        Client.getStage().getScene().setRoot(AccountMenu.getRoot());
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
}
