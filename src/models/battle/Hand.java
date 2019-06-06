package models.battle;

import models.cards.Card;
import views.MyPrinter;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;
    private Card nextOne;
    private int numOfCards;

    public Hand(ArrayList<Card> cards, Card nextOne) {
        this.cards = cards;
        this.nextOne = nextOne;
        this.numOfCards =0;
    }
    public Hand() {
        this.cards = new ArrayList<>();
        this.nextOne =null;
        this.numOfCards =0;
    }
    public void addCard(Card newCard){
        cards.add(newCard);
    }
    public Card getNextOne() {
        return nextOne;
    }
    public void setNextOne(Card nextOne) {
        this.nextOne = nextOne;
    }
    public void show(){
        for (int i = 0; i < cards.size(); i++) {
            MyPrinter.yellow((i+1)+"- "+cards.get(i).getCardId()+" - mana: "+cards.get(i).getMana());
        }
    }
    public void showNextOne(){
        MyPrinter.yellow(nextOne.getCardId()+" -name: "+nextOne.getName());
    }
    public void removeCard(Card selectedCard){
        for (Card card:cards){
            if (card==selectedCard){
                cards.remove(selectedCard);
                return;
            }
        }
    }
    public int size(){
        return cards.size();
    }
    public Card find_card_by_id(String cardId){
        for (Card card:cards){
            if (card.getCardId().equals(cardId))return card;
        }
       return null;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
