import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import defentions.Defentions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.Account;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.deck.Deck;

import java.util.ArrayList;

public class Main extends Application {

    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("layouts/mainPageLayout.fxml"));
        root.getStylesheets().add("resources/stylesheets/mainPageStyle.css");
        root.getStyleClass().add("mainPage");
        Image img = new Image(getClass().getResourceAsStream("resources/cursor.png"));
        ImageCursor cursor = new ImageCursor(img, 30, 30);
        root.setCursor(cursor);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("DUELYST");
        primaryStage.setScene(new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight()));
        primaryStage.show();
    }


    public static void main(String[] args) {
//        runConsole();
        launch(args);
    }

    private static void runConsole() {
        MainMenu mainMenu = new MainMenu();
        Account Mmd = new Account("Mmd");
        Deck deck1 = new Deck("best");
        ArrayList<Minion> minions = Defentions.defineMinion();
        ArrayList<Hero> heroes = Defentions.defineHero();
        ArrayList<Spell> spells = Defentions.defineSpell();
        for (int i = 0; i < 15; i++) {
            deck1.addCard(minions.get(i));
        }
        for (int i = 0; i < 4; i++) {
            deck1.addCard(spells.get(i));
        }
        deck1.addCard(heroes.get(0));
        Mmd.addDeck(deck1);
        Mmd.setMainDeck(deck1);
        AccountMenu.addAccount(Mmd);
        AccountMenu.setLoginAccount(Mmd);
        mainMenu.openMenu();
    }
}
