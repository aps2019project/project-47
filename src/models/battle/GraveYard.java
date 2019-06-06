package models.battle;

import models.cards.Card;
import views.MyPrinter;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraveYard {
    private ArrayList<Card> cards;

    public GraveYard() {
        this.cards = new ArrayList<>();
    }

    public void openMenu(){
        Scanner scanner=new Scanner(System.in);
        help();
        while (true){
            String commandTxt=scanner.nextLine();
            if (commandTxt.equals("exit"))return;
            if (commandTxt.equals("show cards")){
                show();
                continue;
            }
            Pattern pattern = Pattern.compile("^show info ([0-9|a-z|A-Z|_])$");
            Matcher matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                String cardId=matcher.group(1);
                Card card=search(cardId);
                if (card==null){
                    MyPrinter.red("there isn't any card by this code!");
                    continue;
                }
                card.show();
            }
            if (commandTxt.equals("help")){
                help();
                continue;
            }
            MyPrinter.red("Invalid commandTxr!");
        }
    }
    public void show(){
        for (Card card:cards){
            card.show();
        }
    }
    public void add(Card card){
        cards.add(card);
    }
    private void help(){
        MyPrinter.blue("1.show cards");
        System.out.println("2.show info <card id>");
        System.out.println("3.help");
        System.out.println("4.exit");
    }
    private Card search(String cardId){
        for (Card card:cards){
            if (card.getCardId().equals(cardId))return card;
        }
        return null;
    }
}
