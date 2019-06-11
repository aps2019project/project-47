package controllers.console;


import models.Account;
import views.MyPrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controllers.console.constants.*;

public class AccountMenu {

    private static AccountMenu ourInstance = new AccountMenu();

    public static AccountMenu getInstance() {
        return ourInstance;
    }

    private static Scanner scanner = new Scanner(System.in);
    private static Pattern pattern;
    private static Matcher matcher;
    private static ArrayList<Account> accounts;
    private static Account loginAccount;
    private static String showLeaderBoardSTR = "show leaderBoard";
    private static String logoutSTR = "logout";
    private static String helpSTR = "help";
    private static String saveSTR = "save";

    public AccountMenu() {
        this.accounts = new ArrayList<Account>();
    }

    public static void addAccount(Account account) {
        accounts.add(account);
    }

    public static void setLoginAccount(Account loginAccount) {
        AccountMenu.loginAccount = loginAccount;
    }

    public static Account getLoginAccount() {
        return loginAccount;
    }

    public static void openMenu() {
        help();
        int exit = -1;
        while (exit != EXIT) {
            String commandTxt = scanner.nextLine();
            exit = doCommand(commandTxt);
        }
    }

    public static int doCommand(String commandTxt) {
        MyPrinter.blue(commandTxt);
        pattern = Pattern.compile("is([^ ]+)");
        matcher = pattern.matcher(commandTxt);
        if (matcher.find()){
            if (findAccount(matcher.group(1)) == null)
                return 0;
            else
                return ACCOUNT_EXISTS;
        }
        //create account
        pattern = Pattern.compile("^create account ([^ ]+) ([^ ]+)$");
        matcher = pattern.matcher(commandTxt);
        if (matcher.find()) {
            String name = matcher.group(1);
            String password = matcher.group(2);
            if (command_creatAccount(name, password)) {
                MyPrinter.green("Your account was created successfully.");
                return ACCOUNT_CREATE_SUCCESSFULLY;
            } else {
                return ACCOUNT_EXISTS;
            }
        }
        //login
        pattern = Pattern.compile("^login ([^ ]+) ([^ ]+)$");
        matcher = pattern.matcher(commandTxt);
        if (matcher.find()) {
            String userName = matcher.group(1);
            String password = matcher.group(2);

            command_login(userName, password);
            return 0;
        }
        //show leaderBoard
        if (commandTxt.equals(showLeaderBoardSTR) || commandTxt.equals("3")) {
            showLeaderBoard();
            return 0;
        }
        //help
        if (commandTxt.equals(helpSTR) || commandTxt.equals("6")) {
            help();
            return 0;
        }
        //logout
        if (commandTxt.equals(logoutSTR) || commandTxt.equals("5")) {
            loginAccount = null;
            System.out.println("You logged out successfully.");
            return 0;
        }
        //quit
        if (commandTxt.equals("exit") || commandTxt.equals("9")) {
            return EXIT;
        }
        //save
        if (commandTxt.equals(saveSTR) || commandTxt.equals("4")) {
            save();
            return 0;
        }
        if (commandTxt.equals("show my collection") || commandTxt.equals("8")) {
            showCollection(AccountMenu.loginAccount);
            return 0;
        }
        //show logged in account
        if (commandTxt.equals("show logged in account") || commandTxt.equals("7")) {
            if (loginAccount == null) {
                MyPrinter.red("No account was logged in yet!");
                return 0;
            }
            System.out.println(loginAccount.getUserName());
            return 0;
        }
        MyPrinter.red("Invalid command!");
        return 0;
    }

    private static void command_login(String userName, String password) {
        Account account = findAccount(userName);
        if (account == null) {
            MyPrinter.red("Invalid userName!");
            return;
        }
        login(account, password);
    }

    private static boolean login(Account account, String password) {
        if (account.checkPassword(password)) {
            loginAccount = account;
            MyPrinter.green("You logged in successfully.");
            return true;
        }
        MyPrinter.red("Wrong password,try again...");
        return false;
    }

    private static void showLeaderBoard() {
        if (accounts.size() == 0) {
            MyPrinter.red("There's no any user yet.");
            return;
        }
        sortAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            MyPrinter.purple(i + 1 + "-UserName:" + accounts.get(i).getUserName() +
                    "-Wins:" + accounts.get(i).numOfWin());
        }
    }

    private static void sortAccounts() {
        for (int i = 0; i < accounts.size(); i++) {
            for (int j = i + 1; j < accounts.size(); j++) {
                if (!accounts.get(i).compare(accounts.get(j))) {
                    Collections.swap(accounts, i, j);
                }
            }
        }
    }//sorting accounts by num of wins.

    public static Account findAccount(String userName) {
        for (Account account : accounts) {
            if (account.getUserName().equals(userName)) return account;
        }
        return null;
    }

    public static boolean command_creatAccount(String userName, String password) {
        if (findAccount(userName) != null || userName.equals("pc")) {
            MyPrinter.red("Taken userName...,make another one.");
            return false;
        }
        Account account = new Account(userName, password);
        addAccount(account);
        return true;
    }

    public static void help() {
        MyPrinter.blue("1. create account [user name]");
        System.out.println("2. login [user name]");
        System.out.println("3. show leaderBoard");
        System.out.println("4. save");
        System.out.println("5. logout");
        System.out.println("6. help");
        System.out.println("7. show logged in account");
        System.out.println("8. show my collection");
        System.out.println("9. exit");
    }

    public static boolean save() {
        MyPrinter.green("Successfully saved.");
        return true;
    }

    private static void showCollection(Account account) {
        if (loginAccount == null) {
            MyPrinter.red("No account was logged in yet!");
            return;
        }
        account.showAllCollection();
    }

}
