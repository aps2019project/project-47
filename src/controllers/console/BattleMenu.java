package controllers.console;

import controllers.MyController;
import controllers.graphical.BattleChooseMenuController;
import defentions.Defentions;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.*;
import models.battle.*;
import models.deck.Deck;
import models.deck.DeckMaker;
import models.item.ItemType;
import views.MyPrinter;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BattleMenu {
    private static Pattern pattern;
    private static Matcher matcher;
    private static Scanner scanner = new Scanner(System.in);

    private static Parent root;
    private static BattleChooseMenuController controller;

    private void help() {
        MyPrinter.blue("1. single custom <mode> <number of flags>");
        System.out.println("2. single story");
        System.out.println("3. multi <opponent userName> <mode> <number of flags>");
        System.out.println("4. help");
        System.out.println("5. exit");
    }

    public MatchResult battle() {
        Account account = AccountMenu.getLoginAccount();
        if (account == null) {
            MyPrinter.red("no account was logged in!");
            return null;
        }
        if (account.getMainDeck() == null) {
            MyPrinter.red("no main deck!");
            return null;
        }
        if (!account.getMainDeck().check_deck_correct()) return null;
        Player player0 = account.makePlayer(0);
        if (!account.checkCorrectyDeck()) return null;
        help();
        while (true) {
            String commandTxt = scanner.nextLine();
            //custom game
            pattern = Pattern.compile("^single custom ([1|2|3]) ([0-9])$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                int mode = Integer.valueOf(matcher.group(1));
                int numberOfFlags = Integer.valueOf(matcher.group(2));
                DeckMaker deckMaker = new DeckMaker(Defentions.defineCard(), Defentions.all_item_by_type(ItemType.usable));
                Deck deck = deckMaker.constructor("pcDeck");
                if (deck == null) continue;
                Player player1 = new Player(1, "PC", deck, false);
                Battle battle = new Battle(player0, player1, MatchType.convert(mode), numberOfFlags);
                MatchResult matchResult = battle.logic();
                return matchResult;
            }
            //story mode
            if (commandTxt.equals("single story") || commandTxt.equals("2")) {
                if (account.getStoryLvl() == StoryGame.endStory) {
                    MyPrinter.red("you have finished story game!");
                    return null;
                }
                StoryGame storyGame = new StoryGame();
                Battle battle = storyGame.story(player0, account.getStoryLvl());
                MatchResult matchResult = battle.logic();
                if (matchResult.getWinner() == 0) {
                    account.addStoryLvl();
                }
                return matchResult;
            }
            //multi player game
            pattern = Pattern.compile("^multi ([0-9|a-z|A-Z]+) (1|2|3) ([0-9])$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                String userName = matcher.group(1);
                int mode = Integer.valueOf(matcher.group(2));
                int numberOfFlags = Integer.valueOf(matcher.group(3));
                Account opponent = AccountMenu.findAccount(userName);
                if (opponent == null) {
                    System.out.println("incorrect userName!");
                    continue;
                }
                Player player1 = opponent.makePlayer(1);
                Battle battle = new Battle(player0, player1, MatchType.convert(mode), numberOfFlags);
                MatchResult matchResult = battle.logic();
                return matchResult;
            }
            //help
            if (commandTxt.equals("help") || commandTxt.equals("4")) {
                help();
                continue;
            }
            //exit
            if (commandTxt.equals("exit") || commandTxt.equals("5")) return null;
            System.out.println("Invalid command!");
        }
    }

    public static Parent getRoot() {
        if (root==null){
            FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("../../layouts/battleChooseMenu.fxml"));
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller = fxmlLoader.getController();
        }

        controller.update();
        return root;
    }
    public MyController getController() {
        return controller;
    }


}
