import defentions.Defentions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import models.Account;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.deck.Deck;

import java.util.ArrayList;

public class Main extends Application {

    private static boolean error=false;
    public static MainMenu mainMenu=new MainMenu();

    @Override
    public void start(Stage primaryStage) throws Exception{

        {
            Parent root = FXMLLoader.load(getClass().getResource("layouts/sample.fxml"));
            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 300, 275));
            primaryStage.show();
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
        }//creating Mmd account

        new MainMenu().openMenu();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
