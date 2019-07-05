package models.battle;

import models.cards.buff.Buff;
import models.cards.Card;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.deck.Deck;
import models.item.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Player {
    private int playerNum;
    private String userName;
    private Deck deck;
    private Hand hand;
    private int[] num_Of_Used_Of_EveryCard;
    private boolean human;
    private int mana;
    private int age;
    private int num_of_turns_with_flags;
    private Hero hero;
    private Item item;
    private GraveYard graveYard;
    private ArrayList<Buff> manaBuff;
    private Minion selectedMinion;
    private ArrayList<Minion> allMinions;

    public Player(int playerNum, String userName, Deck deck, boolean human) {
        this.num_Of_Used_Of_EveryCard = new int[400];
        for (int i = 0; i < 400; i++) {
            num_Of_Used_Of_EveryCard[i]=1;
        }
        this.playerNum = playerNum;
        this.userName = userName;
        this.deck = deck;
        this.item=deck.getItem();
        allMinions=deck.getAllMinions();
        deck.setCardPlayerNum(playerNum);
        this.hero = deck.giveHero();
        make_Id(this.hero);
        this.hand = new Hand();
        startHand();
        graveYard=new GraveYard();
        manaBuff=new ArrayList<>();
        selectedMinion=null;
        this.human = human;
        this.mana = 0;
        this.age = 0;
        this.num_of_turns_with_flags = 0;
    }

    public ArrayList<Minion> getAllMinions() {
        return allMinions;
    }
    public void add_num_of_turns_with_flags(int value){
        num_of_turns_with_flags+=value;
    }
    public int getNum_of_turns_with_flags() {
        return num_of_turns_with_flags;
    }
    public void addFlagTurns(int value){
        num_of_turns_with_flags=+value;
    }

    public GraveYard getGraveYard() {
        return graveYard;
    }

    public Hero getHero() {
        return hero;
    }
    public void setHero(Hero hero) {
        this.hero = hero;
        make_Id(hero);
    }
    public void addGraveyard(Card card){
        this.graveYard.add(card);
    }
    public void openGraveYard(){
        this.graveYard.openMenu();
    }
    public void startHand(){
        Random random=new Random();
        for (int i = 0; i < 5; i++) {
            int number=random.nextInt(deck.size());
            Card card=deck.giveCard(number);
            make_Id(card);
            hand.addCard(card);
        }
        int number=random.nextInt(deck.size());
        Card card=deck.giveCard(number);
        make_Id(card);
        hand.setNextOne(card);
    }
    public void addManaBuff(Buff buff){
        manaBuff.add(buff);
    }
    public void showHand(){
        hand.show();
    }
    private void make_Id(Card card){
        int code=card.getCode();
        card.setCardId(userName+"_"+card.getName()+"_"+num_Of_Used_Of_EveryCard[code]);
        num_Of_Used_Of_EveryCard[code]++;
    }
    public void showNextOne(){
        hand.showNextOne();
    }
    public String getUserName() {
        return userName;
    }
    public Hand getHand() {
        return hand;
    }
    public int getPlayerNum() {
        return playerNum;
    }
    public boolean isHuman() {
        return human;
    }
    public void all_works_of_aNewTurn(){
        age++;
        mana_rise(Battle.manaEachTurn);
        handle_handCards_firstOfEach_turn();
        Iterator iterator=manaBuff.iterator();
        while (iterator.hasNext()){
            Buff buff=(Buff) iterator.next();
            if (buff.getStartDelay()==0)mana_rise(buff.getPower());
            if (buff.passOneTurn())iterator.remove();
        }
    }
    public void mana_rise(int value){
        if (mana+value>Battle.maxMana){
            mana=Battle.maxMana;
        }else {
            mana = mana + value;
        }
    }
    public boolean have_enough_mana(int value){
        if (mana<value)return false;
        return true;
    }
    public void mana_use(int value){
        mana-=value;
    }
    public void handle_handCards_firstOfEach_turn(){
        while (hand.size()!=5){
            if (hand.getNextOne()==null)return;
            hand.addCard(hand.getNextOne());
            hand.setNextOne(null);
            Card newCard=deck.giveArandomCard();
            if (newCard==null)return;
            make_Id(newCard);
            hand.setNextOne(newCard);
        }
    }
    public int getMana() {
        return mana;
    }
    public Minion getSelectedMinion() {
        return selectedMinion;
    }
    public void setSelectedMinion(Minion selectedMinion) {
        this.selectedMinion = selectedMinion;
    }

    public Item getItem() {
        return item;
    }

}
