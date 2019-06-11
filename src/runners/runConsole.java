package runners;

import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import defentions.Defentions;
import models.Account;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.deck.Deck;

import java.util.ArrayList;

public class runConsole {

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        {
            Account Mmd = new Account("Mmd","1234");
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
        }
        mainMenu.openMenu();
    }
}
