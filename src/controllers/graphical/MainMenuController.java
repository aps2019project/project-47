package controllers.graphical;

import controllers.console.AccountMenu;
import controllers.console.BattleMenu;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Shop;
import runners.Main;

import java.io.IOException;
import java.net.URL;
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

    public void goToCustomCardMenu(MouseEvent mouseEvent) {
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void saveAccount(MouseEvent mouseEvent) {
    }
}
