package controllers.graphical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.console.AccountMenu;
import controllers.console.BattleMenu;
import defentions.Defentions;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import layouts.AlertHelper;
import models.Account;
import models.Shop;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.item.Item;
import runners.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Formatter;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    public Label customCardLabel;
    public Label playLabel;
    public Label collectionLabel;
    public ImageView playBullet;
    public ImageView collectionPlay;
    public ImageView customCard;
    public ImageView exitImage;
    public Label lbl_exit;
    public ImageView saveAccount;
    public Label saveAccountLabel;
    public Account loginAccount = AccountMenu.getLoginAccount();

    public static GsonBuilder gsonBuilder;
    public static Gson gson;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void goToPlayMenu() {
        Main.getStage().getScene().setRoot(BattleMenu.getRoot());
    }

    public void goToCollectionMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/layouts/Collection.fxml"));
        Main.getStage().getScene().setRoot(root);
    }

    public void goToShop() {
        Main.getStage().getScene().setRoot(Shop.getRoot());
    }

    public void goToHistoryMenu() {
    }

    public void logOut() {
        AccountMenu.setLoginAccount(null);
        Main.getStage().getScene().setRoot(AccountMenu.getRoot());
    }

    public void goToCustomCardMenu(MouseEvent mouseEvent) throws IOException {
        Main.getStage().getScene().setRoot(FXMLLoader.load(getClass().getResource("../../layouts/customCardCreatePage.fxml")));
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void saveAccount(MouseEvent mouseEvent) {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        String json = gson.toJson(loginAccount);
        try {
            Formatter formatter = new Formatter("src/JSONs/Accounts/" + loginAccount.getUserName() + ".json");
            formatter.format(json);
            formatter.flush();
            formatter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, Main.getStage().getOwner(), "Account Saved!", "Account " + loginAccount.getUserName() + " Saved!");
    }
}
