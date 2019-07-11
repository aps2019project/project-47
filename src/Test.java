import com.gilecode.yagson.YaGson;
import controllers.console.AccountMenu;
import defentions.Defentions;
import models.Account;
import models.Shop;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.deck.Deck;
import models.item.Item;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;

public class Test {
    public static void main(String[] args) {
        Shop shop = Shop.getInstance();
        Account Mmd = new Account("q", "q");
        Deck deck1 = new Deck("best");
        ArrayList<Minion> minions = new ArrayList<>(Defentions.defineMinion().keySet());
        ArrayList<Hero> heroes = new ArrayList<>(Defentions.defineHero().keySet());
        ArrayList<Spell> spells = new ArrayList<>(Defentions.defineSpell().keySet());
        ArrayList<Item> items = new ArrayList<>(Defentions.defineItem().keySet());
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
//        Deck deck2 = new Deck("best2");
//        for (int i = 0; i < 15; i++) {
//            deck2.addCard(minions.get(i));
//        }
//        for (int i = 0; i < 4; i++) {
//            deck2.addCard(spells.get(i));
//        }
//        deck2.addCard(heroes.get(0));
//        deck2.setItem(items.get(0));
//        Mmd.addDeck(deck2);
        AccountMenu.addAccount(Mmd);
        AccountMenu.setLoginAccount(Mmd);
        for (int i = 101; i < 500; i++) {
            shop.command_buy(i);
        }
        YaGson yaGson = new YaGson();
        String json = yaGson.toJson(Mmd);
        try {
            Path path = Paths.get("src/JSONs/Accounts/" + Mmd.getUserName() + ".json");
            FileOutputStream out = new FileOutputStream(path.toString());
            Formatter formatter = new Formatter(out);
            formatter.format(json);
            formatter.flush();
            formatter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}