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
import runners.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.ResourceBundle;
import java.util.Scanner;

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
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        File file = new File("src/JSONs/Accounts/");
        for (File file1 : file.listFiles()){
            if (file1.getName().contains(".json")){
                String json = "";
                try {
                    Scanner scanner = new Scanner(file1);
                    json = scanner.nextLine();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Account account = gson.fromJson(json, Account.class);
                AccountMenu.addAccount(account);
            }
        }

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
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, Main.getStage().getOwner(), "Account Saved!", "Account " + loginAccount.getUserName() + " Saved!");
    }
}
