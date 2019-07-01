package models.deck;

import controllers.Constants;
import models.cards.Card;
import models.item.Item;
import views.MyPrinter;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMaker {
    ArrayList<Card> cards;
    private ArrayList<Item> items;
    ArrayList<Card> myCards;
    Item myItem;
    private static Scanner scanner=new Scanner(System.in);
    private static Pattern pattern;
    private static Matcher matcher;

    public DeckMaker(ArrayList<Card> cards, ArrayList<Item> items) {
        this.cards = cards;
        this.items = items;
        myCards=new ArrayList<>();
        myItem=null;
    }
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
    public Deck constructor(String name){
        help1();
        while (true){
            String commandTxt=scanner.nextLine();
            if (commandTxt.equals("print all cards") || commandTxt.equals("1")){
                print_all_cards();
                continue;
            }
            if (commandTxt.equals("my cards") || commandTxt.equals("2")){
                print_my_cards();
                continue;
            }
            pattern = Pattern.compile("^select ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                int code=Integer.valueOf(matcher.group(1));
                select(code);
                continue;
            }
            pattern = Pattern.compile("^select ([0-9]+) to ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                int first=Integer.valueOf(matcher.group(1));
                int end=Integer.valueOf(matcher.group(2));
                for (int code = first; code <=end ; code++) {
                    select(code);
                }
                continue;
            }
            pattern = Pattern.compile("^delete ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                int code=Integer.valueOf(matcher.group(1));
                delete(code);
                continue;
            }
            if (commandTxt.equals("help") || commandTxt.equals("6")){
                help1();
                continue;
            }
            if (commandTxt.equals("next") || commandTxt.equals("7")){
                if (new Deck(name,myCards,null).check_deck_correct() != Constants.CORRECT_DECK) break;
                continue;
            }
            if (commandTxt.equals("exit") || commandTxt.equals("8"))return null;
            MyPrinter.red("Invalid commandTxt!");
        }
        MyPrinter.green("now select your item");
        help2();
        while (true){
            String commandTxt=scanner.nextLine();
            if (commandTxt.equals("print all items") || commandTxt.equals("1")){
                printItems();
                continue;
            }
            if (commandTxt.equals("no item") || commandTxt.equals("2")){
                myItem=null;
                break;
            }
            pattern = Pattern.compile("^select ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                int code=Integer.valueOf(matcher.group(1));
                boolean flag=false;
                for (Item item :items){
                    if (item.getCode()==code){
                        myItem=item;
                        flag=true;
                        break;
                    }
                }
                if (flag)break;
                MyPrinter.red("there isn't any card by this code!");
                continue;
            }
            if (commandTxt.equals("help") || commandTxt.equals("4")){

            }
            if (commandTxt.equals("exit") || commandTxt.equals("5"))return null;
            MyPrinter.red("Invalid command!");
        }
        return new Deck(name,myCards,myItem);
    }
    public void help1(){
        MyPrinter.blue("1- print all cards");
        System.out.println("2- my cards");
        System.out.println("3- select <card code>");
        System.out.println("4- select <card code> to <card code>");
        System.out.println("5- delete <card code>");
        System.out.println("6- help");
        System.out.println("7- next");
        System.out.println("8- exit");
    }
    public void help2(){
        MyPrinter.green("1. print all items");
        System.out.println("2. no item");
        System.out.println("3. select <code>");
        System.out.println("4. help");
        System.out.println("5. exit");
    }
    public void print_all_cards(){
        MyPrinter.cyan("");
        for (Card card:cards){
            System.out.print(card.getCode()+" : ");
            card.showInfo();
        }
    }
    public void print_my_cards(){
        MyPrinter.cyan("");
        for (Card card:myCards){
            System.out.print(card.getCode()+" : ");
            card.showInfo();
        }
        int counter = myCards.size();
        if (counter==20){
            MyPrinter.green("number of cards is 20");
        }else {
            MyPrinter.red("number of cards is "+counter);
        }
    }
    public void printItems(){
        MyPrinter.cyan("");
        for (Item item:items){
            System.out.print(item.getCode()+" : ");
            item.show();
        }
    }
    public void select(int code){
        boolean flag=false;
        for (Card card:myCards){
            if (card.getCode()==code){
                MyPrinter.red("you have selected this card in past!choose another");
                flag=true;
            }
        }
        if (flag)return;
        flag=false;
        for (Card card:cards){
            if (card.getCode()==code){
                myCards.add(card);
                MyPrinter.green("the card was added successfully.");
                flag=true;
            }
        }
        if (flag)return;
        MyPrinter.red("there isn't any card by this code!");
    }
    public void delete(int code){
        for (Card card:myCards){
            if (card.getCode()==code){
                myCards.remove(card);
                MyPrinter.green("card deleted successfully!");
                return;
            }
        }
        MyPrinter.red("there isn't any card by this code in your cards!");
    }
}
