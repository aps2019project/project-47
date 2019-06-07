package controllers.console;


import models.Account;
        import views.MyPrinter;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Scanner;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

public class AccountMenu {

    private static Scanner scanner=new Scanner(System.in);
    private static Pattern pattern;
    private static Matcher matcher;
    private static ArrayList<Account> accounts;
    private static Account loginAccount;
    private static String createAccountSTR ="^create account ([^ ]+)$";
    private static String loginSTR ="^login ([^ ]+)$";
    private static String passwordSTR="^[0-9]+$";
    private static String showLeaderBoardSTR ="show leaderBoard";
    private static String logoutSTR ="logout";
    private static String helpSTR ="help";
    private static String saveSTR ="save";

    public AccountMenu() {
        this.accounts = new ArrayList<Account>();
    }
    public static void addAccount(Account account){
        accounts.add(account);
    }
    public static void setLoginAccount(Account loginAccount) {
        AccountMenu.loginAccount = loginAccount;
    }
    public static Account getLoginAccount() {
        return loginAccount;
    }
    public static void openMenu(){
        help();
        while (true){
            String commandTxt=scanner.nextLine();
            //create account
            pattern = Pattern.compile(createAccountSTR);
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                String name=matcher.group(1);
                if (findAccount(name)!=null || name.equals("pc")){
                    MyPrinter.red("Taken userName...,make another one.");
                    continue;
                }
                if (creatAccount(name)){
                    MyPrinter.green("Your account was created successfully.");
                }
                continue;
            }
            //login
            pattern = Pattern.compile(loginSTR);
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()){
                String userName=matcher.group(1);
                Account account=findAccount(userName);
                if (account==null){
                    MyPrinter.red("Invalid userName!");
                    continue;
                }
                if (login(account)){
                    MyPrinter.green("You logged in successfully.");
                }
                continue;
            }
            //show leaderBoard
            if (commandTxt.equals(showLeaderBoardSTR) || commandTxt.equals("3")){
                showLeaderBoard();
                continue;
            }
            //help
            if (commandTxt.equals(helpSTR) || commandTxt.equals("6")){
                help();
                continue;
            }
            //logout
            if (commandTxt.equals(logoutSTR) || commandTxt.equals("5")){
                loginAccount=null;
                System.out.println("You logged out successfully.");
                continue;
            }
            //quit
            if (commandTxt.equals("exit") || commandTxt.equals("9")){
                return;
            }
            //save
            if (commandTxt.equals(saveSTR) || commandTxt.equals("4")){
                save();
                continue;
            }
            if (commandTxt.equals("show my collection")||commandTxt.equals("8")){
                showCollection(AccountMenu.loginAccount);
                continue;
            }
            //show logged in account
            if (commandTxt.equals("show logged in account") || commandTxt.equals("7")){
                if (loginAccount==null){
                    MyPrinter.red("No account was logged in yet!");
                    continue;
                }
                System.out.println(loginAccount.getUserName());
                continue;
            }
            MyPrinter.red("Invalid command!");
        }
    }
    private static boolean login (Account account){
        MyPrinter.green("Put your password to log in or 'back' to return to Account-menu.");
        while (true) {
            String commadnTxt = scanner.nextLine();
            pattern = Pattern.compile(passwordSTR);
            matcher = pattern.matcher(commadnTxt);
            if (matcher.find()){
                if (account.checkPassword(commadnTxt)){
                    loginAccount = account;
                    return true;
                }
                MyPrinter.red("Wrong password,try again...");
                continue;
            }
            if (commadnTxt.equals("back")){
                return false;
            }
            MyPrinter.red("Invalid password...(password is only digits:0-9)");
        }
    }
    private static void showLeaderBoard(){
        if (accounts.size()==0){
            MyPrinter.red("There's no any user yet.");
            return;
        }
        sortAccounts();
        for (int i=0;i<accounts.size();i++){
            MyPrinter.purple(i+1+"-UserName:"+accounts.get(i).getUserName()+
                    "-Wins:"+accounts.get(i).numOfWin());
        }
    }
    private static void sortAccounts(){
        for (int i=0;i<accounts.size();i++){
            for (int j=i+1;j<accounts.size();j++){
                if (!accounts.get(i).compare(accounts.get(j))){
                    Collections.swap(accounts,i,j);
                }
            }
        }
    }//sorting accounts by num of wins.
    public static Account findAccount(String userName){
        for (Account account:accounts){
            if (account.getUserName().equals(userName))return account;
        }
        return null;
    }
    public static boolean creatAccount(String userName){
        MyPrinter.yellow("Put your password to create an account or 'back' to return to Account-menu.");
        while (true) {
            String commadnTxt = scanner.nextLine();
            pattern = Pattern.compile(passwordSTR);
            matcher = pattern.matcher(commadnTxt);
            if (matcher.find()){
                Account account=new Account(userName);
                    account.setPassword(commadnTxt);
                    addAccount(account);
                return true;
            }
            if (commadnTxt.equals("back")){
                return false;
            }
            MyPrinter.red("Invalid password...(password is only digits:0-9)");
        }
    }
    public static void help(){
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
    public static boolean save(){
        MyPrinter.green("Successfully saved.");
        return true;
    }
    private static void showCollection(Account account){
        if (loginAccount==null){
            MyPrinter.red("No account was logged in yet!");
            return;
        }
        account.showAllCollection();
    }

}
