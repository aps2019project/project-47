package models;

import controllers.console.AccountMenu;
import models.cards.Card;
import models.deck.Deck;
import models.deck.DeckMaker;
import models.item.Item;
import views.MyPrinter;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Collection {
    private static Pattern pattern;
    private static Matcher matcher;
    private static Scanner scanner=new Scanner(System.in);

    public void openMenu(){
        Account account= AccountMenu.getLoginAccount();
        if (account==null){
            MyPrinter.red("no account was logged in!");
            return;
        }
        help();
        while (true){
            String commandTxt=scanner.nextLine();
            if (commandTxt.equals("show all cards and items") || commandTxt.equals("1")){
                for (Card card:account.getCards()){
                    card.show();
                }
                for (Item item:account.getItems()){
                    item.show();
                }
                continue;
            }
            if(commandTxt.equals("decks") || commandTxt.equals("2")){
                for (Deck deck:account.getDecks()){
                    MyPrinter.cyan(deck.getName());
                }
                continue;
            }
            pattern = Pattern.compile("^delete ([0-9|a-z|A-Z]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                String name=matcher.group(1);
                boolean flag=false;
                for (Deck deck:account.getDecks()){
                    if (deck.getName().equals(name)){
                        account.getDecks().remove(deck);
                        MyPrinter.green("deck deleted successfully!");
                        flag=true;
                        break;
                    }
                }
                if (!flag){
                    MyPrinter.red("there isn't any deck by this name!");
                }
                continue;
            }
            pattern = Pattern.compile("^new deck ([0-9|a-z|A-Z]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                boolean flag=false;
                String name=matcher.group(1);
                for (Deck deck:account.getDecks()){
                    if (deck.getName().equals(name)){
                        flag= true;
                        break;
                    }
                }
                if (flag){
                    MyPrinter.red("taken name!choose another name.");
                    continue;
                }
                Deck deck=new DeckMaker(account.getCards(),account.getItems()).constructor(name);
                if (deck!=null) {
                    account.addDeck(deck);
                    MyPrinter.green(name + " was added successfully!");
                    continue;
                }
            }
            pattern = Pattern.compile("^set main ([0-9|a-z|A-Z]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                String name = matcher.group(1);
                boolean flag=false;
                for (Deck deck:account.getDecks()){
                    if (deck.getName().equals(name)){
                        account.setMainDeck(deck);
                        MyPrinter.green(name+" is main deck.");
                        flag=true;
                        break;
                    }
                }
                if (!flag){
                    MyPrinter.red("there isn't any deck by this name!");
                }
                continue;
            }
            pattern = Pattern.compile("^show ([0-9|a-z|A-Z]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                boolean flag=false;
                for (Deck deck:account.getDecks()){
                    if (deck.getName().equals(matcher.group(1))){
                        deck.show();
                        flag=true;
                        break;
                    }
                }
                if (flag)continue;
                MyPrinter.red("there isn't any deck by this name!");
                continue;
            }
            if (commandTxt.equals("help") || commandTxt.equals("7")){
                help();
                continue;
            }
            if (commandTxt.equals("exit") || commandTxt.equals("8")){
                return;
            }
            MyPrinter.red("Invalid commandTxt");
        }
    }
    public void help(){
        MyPrinter.blue("1. show all cards and items");
        System.out.println("2. decks");
        System.out.println("3. delete <deck name>");
        System.out.println("4. new deck <deck name>");
        System.out.println("5. set main <deck name>");
        System.out.println("6. show <deck name>");
        System.out.println("7. help");
        System.out.println("8. exit");
    }

}
