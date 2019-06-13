package models;

import controllers.MyController;
import defentions.Defentions;
import controllers.console.AccountMenu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import models.cards.Card;
import models.item.Item;
import models.item.ItemType;
import views.MyPrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Shop {

    private static Shop ourInstance = new Shop();

    public static Shop getInstance() {
        return ourInstance;
    }

    private Scene scene;
    private MyController controller;

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
                command_show_shop_collection();
                continue;
            }
            if (commandTxt.equals("show my collection")||commandTxt.equals("5")){
                command_show_my_collection(account);
                continue;
            }
            pattern = Pattern.compile("^buy ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                command_buy(Integer.valueOf(matcher.group(1)));
                continue;
            }
            pattern = Pattern.compile("^buy ([0-9]+) to ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                int x= Integer.valueOf(matcher.group(1));
                int y=Integer.valueOf(matcher.group(2));
                for (int i = x; i <=y ; i++) {
                command_buy(i);
                }
                continue;
            }

            pattern = Pattern.compile("^sell ([0-9]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                command_sell(Integer.valueOf(matcher.group(1)));
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

    public void command_show_my_collection(Account account) {
        if (account==null){
        MyPrinter.red("no account was logged in!");
            return;
        }
        account.showAllCollection();
    }

    public void help(){
        MyPrinter.blue("1. command_buy <code>");
        System.out.println("2. command_buy <code> to <code>");
        System.out.println("3. command_sell <code>");
        System.out.println("4. command_show_shop_collection shop collection");
        System.out.println("5. command_show_shop_collection my collection");
        System.out.println("6. help");
        System.out.println("7. earn <value>");
        System.out.println("8. exit");
    }
    private void command_show_shop_collection(){
        for (Card card:cards){
            MyPrinter.cyan("code : "+card.getCode()+" , price : "+card.getPrice());
            card.showInfo();
        }
        for (Item item:items){
            MyPrinter.cyan("code : "+item.getCode()+" , price : "+item.getPrice());
            item.show();
        }
    }

    //code -1 for null account
    //code -2 for repetitive buy
    //code -3 for not enough money
    //code 1 for successful buy
    public int command_buy(int code){
        Account account= AccountMenu.getLoginAccount();
        if (account==null){
            MyPrinter.red("no account was logged in!");
            return -1;
        }
        Object object=find_in_shop(code);
        if (object==null){
            MyPrinter.red("there isn't any thing by this code in shop!");
            return -9;
        }
        if (account.search(code)!=null){
            MyPrinter.red("you bought it in past!");
            return -2;
        }
        int price=determine_price(code);
        if (account.getMoney()<price){
            MyPrinter.red("you haven't enough money to command_buy it.");
            return -3;
        }
        account.buy(price);
        MyPrinter.green("selected thing was bought successfully!");
        account.addCardOrItem(object);
        return 1;
    }
    public int determine_price(int code){
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
    public Object find_in_shop(int code){
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
    public void command_sell(int code){
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

    public Scene getScene() {
        if (scene==null){
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                Parent p = fxmlLoader.load(getClass().getResource("foo.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller = (MyController) fxmlLoader.getController();
        }
        return scene;
    }

    public MyController getController() {
        return controller;
    }
}
