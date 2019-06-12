package controllers.graphical;

import controllers.console.AccountMenu;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import runners.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    public Label playLabel;
    public Label collectionLabel;
    public ImageView playBullet;
    public ImageView collectionPlay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void goToPlayMenu() {

    }

    public void goToCollectionMenu() {

    }

    public void goToShop() {

    }

    public void goToHistoryMenu() {
    }

    public void logOut() throws IOException {
        AccountMenu.setLoginAccount(null);
        Parent root =  FXMLLoader.load(getClass().getResource("../../layouts/accountPage.fxml"));
        Main.getStage().getScene().setRoot(root);
    }
}
