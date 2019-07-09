package models;

import controllers.Constants;
import controllers.console.MainMenu;
import controllers.graphical.UniversalShopController;
import models.battle.MatchResult;
import models.battle.Player;
import models.cards.Card;
import models.deck.Deck;
import models.item.Item;
import views.MyPrinter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class Account implements Cloneable {
    private String userName;
    private int money;
    private String password;
    private transient String authToken;

    private ArrayList<MatchResult> matchHistorys;

    private ArrayList<Card> cards;
    private ArrayList<Item> items;
    private ArrayList<Deck> decks;
    private int storyLvl;
    private Deck mainDeck;

    public Account(String userName, String password) {
        this.userName = new String(userName);
        this.money = 300_000;
        storyLvl = 1;
        this.matchHistorys = new ArrayList<MatchResult>();
        cards = new ArrayList<>();
        items = new ArrayList<>();
        this.decks = new ArrayList<Deck>();
        this.password = password;
    }


    private transient static HashMap<String, Account> accountsMapper = new HashMap<>(); //token --> account

    public static synchronized void putAccount(String authToken, Account account) {
        accountsMapper.put(authToken, account);
    }

    public static HashMap<String, Account> getAccountsMapper() {
        return accountsMapper;
    }

    public static String generateRandomString(int length) {
        SecureRandom generator = new SecureRandom();
        String result = "";
        for (int i = 0; i < length; i++) {
            result = result.concat(hexadecimalCharacter(generator.nextInt(16)));
        }
        return result;
    }

    public String getPassword() {
        return password;
    }

    private static String hexadecimalCharacter(int code) {
        code %= 16;
        if (code < 10)
            return Integer.toString(code);
        char ch = (char) (code % 10);
        ch += 'A';
        return Character.toString(ch);
    }

    public int getWins() {
        int count = 0;
        for (MatchResult matchResult : this.matchHistorys) {
            if (matchResult.getUser0().equals(userName)) {
                if (matchResult.getWinner() == 0) count++;
            }
            if (matchResult.getUser1().equals(userName)) {
                if (matchResult.getWinner() == 1) count++;
            }
        }
        return count;
    }

    public int getLoses(){
        int count = 0;
        for (MatchResult matchResult : this.matchHistorys) {
            if (matchResult.getUser1().equals(userName)) {
                if (matchResult.getWinner() == 0) count++;
            }
            if (matchResult.getUser0().equals(userName)) {
                if (matchResult.getWinner() == 1) count++;
            }
        }
        return count;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getStoryLvl() {
        return storyLvl;
    }

    public ArrayList<MatchResult> getMatchHistorys() {
        if (matchHistorys==null){
            matchHistorys= new ArrayList<>();
        }
        return matchHistorys;
    }

    public Deck getMainDeck() {
        return mainDeck;
    }

    public void setMainDeck(Deck maindeck) {
        mainDeck = maindeck;
    }

    public void addStoryLvl() {
        money += MainMenu.victoryPrize * storyLvl;
        this.storyLvl++;
    }

    public void addCardOrItem(Object object) {
        if (object instanceof Card)
            this.cards.add((Card) object);
        if (object instanceof Item)
            this.items.add((Item) object);
    }

    public int getMoney() {
        return money;
    }

    public void buy(int value) {
        this.money -= value;
    }

    public void addMatchResult(MatchResult matchResult) {
        this.matchHistorys.add(matchResult);
    }

    public boolean compare(Account account) {
        if (this.getWins() > account.getWins()) return true;
        if (this.getLoses() < account.getLoses()) return false;
        if (this.userName.compareTo(account.userName) < 0) return true;
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public boolean checkPassword(String password) {
        if (this.password.equals(password)) return true;
        return false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Player makePlayer(int playerNum) {
        Deck deck = this.mainDeck.clone();
        Player player = new Player(playerNum, this.userName, deck, true);
        return player;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void moneyRise(int value) {
        money = money + value;
    }

    public void showAllCollection() {
        MyPrinter.green("cards :");
        for (Card card : cards) {
            card.show();
        }
        MyPrinter.green("items :");
        for (Item item : items) {
            item.show();
        }
    }

    public Object search(int code) {
        for (Card card : cards) {
            if (card.getCode() == code) return card;
        }
        for (Item item : items) if (item.getCode() == code) return item;
        return null;
    }

    public boolean sellCardOrItem(int code) {
        for (Card card : cards) {
            if (card.getCode() == code) {
                cards.remove(card);
                moneyRise(card.getPrice());
                for (Card card1 : Shop.getInstance().getCards().keySet()) {
                    if (card1.getName().equals(card.getName())) {
                        Shop.getInstance().getCards().replace(card1, Shop.getInstance().getCards().get(card1) + 1);
                        break;
                    }
                }
                try {
                    UniversalShopController.instance.setUniversalCollectionMenu();
                } catch (NullPointerException ignored) {
                }
                return true;
            }
        }
        for (Item item : items) {
            if (item.getCode() == code) {
                items.remove(item);
                moneyRise(item.getCode());
                for (Item item1 : Shop.getInstance().getItems().keySet()) {
                    if (item1.getName().equals(item.getName())) {
                        Shop.getInstance().getItems().replace(item, Shop.getInstance().getItems().get(item1) + 1);
                        break;
                    }
                }
                try {
                    UniversalShopController.instance.setUniversalCollectionMenu();
                } catch (NullPointerException ignored) {
                }
                return true;
            }
        }
        return false;
    }

    public boolean checkCorrectyDeck() {
        if (this.getMainDeck() == null) {
            System.out.println("main decks there not exist");
            return false;
        }
        if (this.getMainDeck().check_deck_correct() != Constants.CORRECT_DECK) {
            System.out.println("main decks is not correct!");
            return false;
        }
        return true;
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public boolean hasDeck(Deck deck) {
        for (Deck deck1 : decks) {
            if (deck.getName().equals(deck1.getName()))
                return true;
        }
        return false;
    }
}
