package runners;

import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import defentions.Defentions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Account;
import models.Shop;
import models.battle.board.Board;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.deck.Deck;
import models.item.Item;
import views.MyPrinter;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Shop shop = Shop.getInstance();
        Account Mmd = new Account("Mmd","1234");
        Deck deck1 = new Deck("best");
        ArrayList<Minion> minions = Defentions.defineMinion();
        ArrayList<Hero> heroes = Defentions.defineHero();
        ArrayList<Spell> spells = Defentions.defineSpell();
        ArrayList<Item> items = Defentions.defineItem();
        for (int i = 0; i < 15; i++) {
            deck1.addCard(minions.get(i));
        }
        for (int i = 0; i < 4; i++) {
            deck1.addCard(spells.get(i));
        }
        deck1.addCard(heroes.get(0));
        deck1.setItem(items.get(0));
        Mmd.addDeck(deck1);
        Mmd.setMainDeck(deck1);
        AccountMenu.addAccount(Mmd);
        AccountMenu.setLoginAccount(Mmd);
        for (int i = 101; i < 500; i++) {
            shop.command_buy(i);
        }
        stage = primaryStage;
//        Parent root = AccountMenu.getRoot();
        Parent root = MainMenu.getRoot();
//        Parent root = Shop.getRoot();
//        Parent root = BattleMenu.getRoot();
//        Parent root = Board.getRoot();
//        Parent root = FXMLLoader.load(getClass().getResource("../layouts/customCardCreatePage.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("../layouts/Collection.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setMaximized(true);
        stage.show();
        System.out.println("\u001B[1000m"+""+"\u001B[1000m");//resetting color
        Image img = new Image("/resources/buttons/cursor.png");
        ImageCursor cursor = new ImageCursor(img, 0, 0);
        stage.getScene().setCursor(cursor);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
