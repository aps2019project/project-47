package controllers.graphical;

import controllers.console.AccountMenu;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void goToPlayMenu() {

    }

    public void goToCollectionMenu() {

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
}
