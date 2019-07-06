package controllers.console;


import controllers.Constants;
import controllers.MyController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.Account;
import models.Collection;
import models.battle.MatchResult;
import models.Shop;
import views.MyPrinter;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controllers.Constants.EXIT;

public class MainMenu {
    private static Pattern pattern;
    private static Matcher matcher;
    private static Parent root;
    private static MyController controller;
    private static Scanner scanner = new Scanner(System.in);

    public static int victoryPrize = 500;
    public static AccountMenu accountMenu = new AccountMenu();
    public static Collection collection = new Collection();
    public static BattleMenu battleMenu = new BattleMenu();

    public static void help() {
        MyPrinter.blue("1.collection");
        System.out.println("2.shop");
        System.out.println("3.battle");
        System.out.println("4.exit");
        System.out.println("5.account settings");
        System.out.println("6.help");
    }

    public void openMenu() {
        accountMenu.openMenu();
        help();
        Constants exit = null;
        while (exit != EXIT) {
            String commandTxt = scanner.nextLine();
            exit = doCommand(commandTxt);
        }
    }

    public Constants doCommand(String commandTxt) {
        if (commandTxt.equals("collection") || commandTxt.equals("1")) {
            collection.openMenu();
            return null;
        }
        if (commandTxt.equals("shop") || commandTxt.equals("2")) {
            Shop.getInstance().open_shopMenu();
            return null;
        }
        if (commandTxt.equals("battle") || commandTxt.equals("3")) {
            MatchResult matchResult = battleMenu.battle();
            if (matchResult == null) return null;
            computingMatchResult(matchResult);
            MyPrinter.green("the battle was finished!");
            return null;
        }
        if (commandTxt.equals("exit") || commandTxt.equals("4")) return EXIT;
        if (commandTxt.equals("account settings") || commandTxt.equals("5")) {
            AccountMenu.openMenu();
            return null;
        }
        if (commandTxt.equals("help") || commandTxt.equals("6")) {
            help();
            return null;
        }
        MyPrinter.red("Invalid command!");
        return null;
    }

    public void computingMatchResult(MatchResult matchResult) {
        String user1 = matchResult.getUser0();
        String user2 = matchResult.getUser1();
        int winner = matchResult.getWinner();
        if (winner == 0) {
            System.out.println(MyPrinter.ANSI_GREEN_BACKGROUND);
            MyPrinter.green("you win!");
            System.out.println(MyPrinter.ANSI_GREEN_BACKGROUND);
            MyPrinter.clearBackground();
        } else {
            System.out.println(MyPrinter.ANSI_RED_BACKGROUND);
            MyPrinter.red("you lose");
            System.out.println(MyPrinter.ANSI_RED_BACKGROUND);
            MyPrinter.clearBackground();
        }
        Account account1 = AccountMenu.findAccount(user1);
        Account account2 = AccountMenu.findAccount(user2);
        if (account1 != null) {
            account1.addMatchResult(matchResult);
            if (winner == 0) account1.moneyRise(victoryPrize);
        }
        if (account2 != null) {
            account2.addMatchResult(matchResult);
            if (winner == 1) account2.moneyRise(victoryPrize);
        }
    }


    public static Parent getRoot() {
        if (root==null){
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                root = fxmlLoader.load(MainMenu.class.getResource("../../layouts/mainMenu.fxml").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller = fxmlLoader.getController();
        }
        return root;
    }
    public static MyController getController() {
        return controller;
    }
}
