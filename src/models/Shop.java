package models;

import defentions.Defentions;
import controllers.console.AccountMenu;
import models.cards.Card;
import models.item.Item;
import models.item.ItemType;
import views.MyPrinter;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shop {
    private static Pattern pattern;
    private static Matcher matcher;
    private static Scanner scanner=new Scanner(System.in);

    private ArrayList<Card> cards;
    private ArrayList<Item> items;

    public Shop() {
        cards= Defentions.defineCard();
        items= Defentions.all_item_by_type(ItemType.usable);
    }

    public void open_shopMenu(){
        Account account= AccountMenu.getLoginAccount();
        help();
        while (true){
            if (account!=null) MyPrinter.green("you have "+account.getMoney()+" $.");
            String commandTxt=scanner.nextLine();

            if (commandTxt.equals("show shop collection") ||commandTxt.equals("4")){
                show();
                continue;
            }
            if (commandTxt.equals("show my collection")||commandTxt.equals("5")){
                if (account==null){
                MyPrinter.red("no account was logged in!");
                continue;
                }
                account.showAllCollection();
                continue;
            }
            pattern = Pattern.compile("^buy ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                buy(Integer.valueOf(matcher.group(1)));
                continue;
            }
            pattern = Pattern.compile("^buy ([0-9]+) to ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                int x= Integer.valueOf(matcher.group(1));
                int y=Integer.valueOf(matcher.group(2));
                for (int i = x; i <=y ; i++) {
                buy(i);
                }
                continue;
            }

            pattern = Pattern.compile("^sell ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                sell(Integer.valueOf(matcher.group(1)));
                continue;
            }
            if (commandTxt.equals("help") ||commandTxt.equals("6")){
                help();
                continue;
            }
            pattern = Pattern.compile("^earn ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                if (account==null){
                    MyPrinter.red("no account was logged in!");
                    continue;
                }
                account.moneyRise(Integer.valueOf(matcher.group(1)));
                continue;
            }
            if (commandTxt.equals("exit")||commandTxt.equals("8")){
                return;
            }
            System.out.println("Invalid commandTxt!");
        }
    }
    private void help(){
        MyPrinter.blue("1. buy <code>");
        System.out.println("2. buy <code> to <code>");
        System.out.println("3. sell <code>");
        System.out.println("4. show shop collection");
        System.out.println("5. show my collection");
        System.out.println("6. help");
        System.out.println("7. earn <value>");
        System.out.println("8. exit");
    }
    private void show(){
        for (Card card:cards){
            MyPrinter.cyan("code : "+card.getCode()+" , price : "+card.getPrice());
            card.showInfo();
        }
        for (Item item:items){
            MyPrinter.cyan("code : "+item.getCode()+" , price : "+item.getPrice());
            item.show();
        }
    }
    private void buy(int code){
        Account account= AccountMenu.getLoginAccount();
        if (account==null){
            MyPrinter.red("no account was logged in!");
            return;
        }
        Object object=find_in_shop(code);
        if (object==null){
            MyPrinter.red("there isn't any thing by this code in shop!");
            return;
        }
        if (account.search(code)!=null){
            MyPrinter.red("you bought it in past!");
            return;
        }
        int price=determine_price(code);
        if (account.getMoney()<price){
            MyPrinter.red("you haven't enough money to buy it.");
            return;
        }
        account.buy(price);
        MyPrinter.green("selected thing was bought successfully!");
        account.addCardOrItem(object);
    }
    private int determine_price(int code){
        for (Card card:cards){
            if (card.getCode()==code){
                return card.getPrice();
            }
        }
        for (Item item:items){
            if (item.getCode()==code){
                return item.getPrice();
            }
        }
        return 0;
    }
    private Object find_in_shop(int code){
        for (Card card:cards){
            if (card.getCode()==code){
                return card;
            }
        }
        for (Item item:items){
            if (item.getCode()==code){
                return item;
            }
        }
        return null;
    }
    private void sell(int code){
        Account account= AccountMenu.getLoginAccount();
        if (account==null){
            MyPrinter.red("no account was logged in!");
            return;
        }
        if (account.sellCardOrItem(code)){
            MyPrinter.green("the card was sold successfully!");
        }else {
            MyPrinter.red("there isn't any card or item by this code!");
        }
    }


}
