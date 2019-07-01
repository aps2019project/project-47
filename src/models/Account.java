package models;

import controllers.console.Constants;
import controllers.console.MainMenu;
import models.battle.MatchResult;
import models.battle.Player;
import models.cards.Card;
import models.deck.Deck;
import models.item.Item;
import views.MyPrinter;

import java.util.ArrayList;

public class Account implements Cloneable {
    private String userName;
    private int money;
    private String password;

    private ArrayList<MatchResult> matchHistory;

    private ArrayList<Card> cards;
    private ArrayList<Item> items;
    private ArrayList<Deck> decks;
    private int storyLvl;
    private Deck mainDeck;
    public Account(String userName, String password) {
        this.userName = new String(userName);
        this.money = 1500000;
        storyLvl=1;
        this.matchHistory = new ArrayList<MatchResult>();
        cards = new ArrayList<>();
        items = new ArrayList<>();
        this.decks = new ArrayList<Deck>();
        this.password = password;
    }

    public int getStoryLvl() {
        return storyLvl;
    }

    public ArrayList<MatchResult> getMatchHistory() {
        return matchHistory;
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
        this.matchHistory.add(matchResult);
    }

    public int numOfWin() {
        int count = 0;
        for (MatchResult matchResult : this.matchHistory) {
            if (matchResult.getUser0().equals(userName)) {
                if (matchResult.getWinner() == 0) count++;
            }
            if (matchResult.getUser1().equals(userName)) {
                if (matchResult.getWinner() == 1) count++;
            }
        }
        return count;
    }

    public boolean compare(Account account) {
        if (this.numOfWin() > account.numOfWin()) return true;
        if (this.numOfWin() < account.numOfWin()) return false;
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
        try {
            Deck deck = (Deck) this.mainDeck.clone();
            Player player = new Player(playerNum, this.userName, deck, true);
            return player;
        } catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
            return null;
        }
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
                return true;
            }
        }
        for (Item item : items) {
            if (item.getCode() == code) {
                items.remove(item);
                moneyRise(item.getCode());
                return true;
            }
        }
        return false;
    }

    public boolean checkCorrectyDeck() {
        if (this.getMainDeck() == null) {
            System.out.println("main deck there not exist");
            return false;
        }
        if (this.getMainDeck().check_deck_correct() != Constants.CORRECT_DECK){
            System.out.println("main deck is not correct!");
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

    public boolean hasDeck(Deck deck){
        for (Deck deck1 : decks){
            if (deck.getName().equals(deck1.getName()))
                return true;
        }
        return false;
    }
}
