package models.deck;

import com.gilecode.yagson.YaGson;
import controllers.Constants;
import models.cards.Card;
import models.cards.CardType;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.item.Item;
import views.MyPrinter;

import java.util.ArrayList;
import java.util.Random;

public class Deck{
    private String name;
    private ArrayList<Card> cards;
    private Item item;

    public Deck(String name, ArrayList<Card> cards, Item item) {
        this.name = name;
        this.cards = cards;
        this.item = item;
    }

    public Deck(String name) {
        this.name = name;
        cards = new ArrayList<Card>();
        item = null;
    }

    public Item getItem() {
        return item;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int size() {
        return cards.size();
    }

    public Card giveCard(int i) {
        Card card = cards.get(i);
        cards.remove(card);
        return card;
    }

    public Card giveArandomCard() {
        if (cards.size() == 0) return null;
        return giveCard(0);
    }

    public Deck clone(){
        YaGson yaGson = new YaGson();
        String clonedStr =  yaGson.toJson(this);
        Deck newDeck =  yaGson.fromJson(clonedStr,Deck.class);
        return newDeck;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void show() {
        MyPrinter.cyan("cards:");
        int i = 1;
        for (Card card : cards) {
            System.out.print(i + "- ");
            card.show();
            i++;
        }
    }

    public Hero giveHero() {
        Hero hero = null;
        for (Card card : cards) {
            if (card.getCardType() == CardType.hero) {
                hero = (Hero) card;
            }
        }
        if (hero == null) return null;
        cards.remove(hero);
        return hero;
    }

    public Card getHero(){
        for (Card card : cards){
            if(card instanceof Hero || card.getCardType() == CardType.hero){
                return card;
            }
        }
        return null;
    }

    public void setCardPlayerNum(int playerNum) {
        for (Card card : cards) {
            card.setPlyNum(playerNum);
        }
    }

    public ArrayList<Minion> getAllMinions() {
        ArrayList<Minion> minions = new ArrayList<>();
        for (Card card : cards) {
            if (card.getCardType() == CardType.minion) minions.add((Minion) card);
        }
        return minions;
    }

    public Constants check_deck_correct() {
        int hero = 0;
        if (cards.size() != 20) {
            MyPrinter.red("num of cards is not 20!");
            return Constants.NOT_20_CARDS;
        }
        for (Card card : cards) {
            if (card.getCardType() == CardType.hero) hero++;
        }
        if (hero < 1) {
            MyPrinter.red("there isn't any hero in this decks");
            return Constants.NO_HERO;
        }
        if (hero > 1){
            MyPrinter.red("you have selected more than one hero");
            return Constants.MULTIPLE_HEROS;
        }
        return Constants.CORRECT_DECK;
    }

    public String getName() {
        return name;
    }
    public boolean remove(String name){
        for (int i = 0; i < cards.size(); i++){
            if (cards.get(i).getName().equals(name)){
                cards.remove(i);
                return true;
            }
        }
        if (item.getName().equals(name)){
            item = null;
            return true;
        }
        return false;
    }
}
