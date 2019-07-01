package controllers.graphical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.console.AccountMenu;
import controllers.console.BattleMenu;
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
import network.Client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainMenuController {
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
            FXMLLoader.load(getClass().getResource("/layouts/MatchHistory.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut() {
        AccountMenu.setLoginAccount(null);
        Client.getStage().getScene().setRoot(AccountMenu.getRoot());
    }

    public void goToCustomCardMenu(MouseEvent mouseEvent) throws IOException {
        Client.getStage().getScene().setRoot(FXMLLoader.load(getClass().getResource("../../layouts/customCardCreatePage.fxml")));
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void saveAccount(MouseEvent mouseEvent) {
        if(loginAccount == null)
            return;
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        String json = gson.toJson(loginAccount);
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
}
