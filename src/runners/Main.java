package runners;

import controllers.console.AccountMenu;
import controllers.console.BattleMenu;
import controllers.console.MainMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Account;
import models.Shop;

import java.io.IOException;

public class Main extends Application {

    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Account account = new Account("Mmd", "1234");
        AccountMenu.addAccount(account);
        AccountMenu.setLoginAccount(account);

        stage = primaryStage;
//        Parent root = BattleMenu.getRoot();
//        Parent root = AccountMenu.getRoot();
//        Parent root = MainMenu.getRoot();
//        Parent root = Shop.getRoot();
        Parent root = FXMLLoader.load(Shop.class.getResource("../layouts/customCardCreatePage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setMaximized(true);
        stage.show();
        Image img = new Image("/resources/buttons/cursor.png");
        ImageCursor cursor = new ImageCursor(img, 0, 0);
        stage.getScene().setCursor(cursor);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
