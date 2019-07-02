package models.battle;

import defentions.Defentions;
import models.battle.board.Board;
import models.battle.board.Cell;
import models.battle.board.Location;
import models.cards.*;
import models.cards.buff.Buff;
import models.cards.minion.*;
import models.cards.spell.TargetForm;
import models.cards.spell.effect.Effect;
import models.cards.spell.effect.HouseEffect;
import models.cards.hero.Hero;
import models.cards.hero.HeroSpecialItemPack;
import models.cards.spell.Spell;
import models.item.Item;
import views.MyPrinter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Battle {
    private int boundOfItems = 3;
    public static final int maxMana = 100;
    public static final int manaEachTurn = 5;
    private static int keepFlagToVictory = 6;
    private static Scanner scanner = new Scanner(System.in);
    private static Pattern pattern;
    private static Matcher matcher;
    //test
    private Player[] players;
    private int turn; //it most be zero or one.
    private Board board;
    private MatchType matchType;
    private MatchResult matchResult;
    private int age;
    private int numOfFlags;

    public Battle(Player player0, Player player1, MatchType matchType, int numOfFlags) {
        this.players = new Player[2];
        players[0] = player0;
        players[1] = player1;
        this.turn = 0;
        this.board = new Board();
        this.matchType = matchType;
        this.matchResult = null;
        this.age = 0;
        this.numOfFlags = numOfFlags;
        //set heroes at their starting locations...
        board.selectCell(Board.hero0).setMinion(player0.getHero());
        player0.getHero().setLocation(Board.hero0);
        board.selectCell(Board.hero1).setMinion(player1.getHero());
        player1.getHero().setLocation(Board.hero1);
        //setting flags on board
        if (matchType != MatchType.kill) {
            board.set_flags_for_random(numOfFlags);
        }
        //setting items on board
        set_items_on_the_board();
        //do items first of each battle
        action_item(find_item_minions_target(0, player0.getItem(), null), player0.getItem());
        action_item(find_item_minions_target(1, player1.getItem(), null), player1.getItem());
    }

    public void set_items_on_the_board() {
        Random random = new Random();
        int b = random.nextInt(boundOfItems);
        for (int i = 0; i < b; i++) {
            int x = random.nextInt(Board.width);
            int y = random.nextInt(Board.length);
            Location location = new Location(x, y);
            if (location.equals(Board.hero0) || location.equals(Board.hero1)) {
                i--;
                continue;
            }
            Item item = Defentions.defineItem().get(random.nextInt(Defentions.defineItem().size()));
            board.selectCell(location).setItem(item);
        }
    }

    public ArrayList<Minion> find_item_minions_target(int playerNum, Item item, Location originLocation) {
        Player player = this.players[playerNum];
        if (originLocation == null) {
            ArrayList<Minion> minions = new ArrayList<>();
            if (item == null) return minions;
            if (item.isAllCards()) {


                minions.addAll(player.getAllMinions());
            } else {
                minions.add(player.getHero());
            }
            return minions;
        }
        return board.find_all_minions_in_target(player.getPlayerNum(), originLocation, item.getTargetForm());
    }

    public void action_item(ArrayList<Minion> minions, Item item) {
        if (item == null) return;
        for (Minion minion : minions) {
            for (Effect effect : item.getEffects()) {
                if (item.isAction()) {
                    give_an_effect_to_a_minion(effect, minion);
                } else {
                    switch (item.getActivateTime()) {
                        case passive: {
                            minion.getSpecialItem().addPassive(effect);
                            break;
                        }
                        case onSpawn: {
                            minion.getSpecialItem().addSpawn(effect);
                            break;
                        }
                        case onDeath: {
                            minion.getSpecialItem().addDeath(effect);
                            break;
                        }
                        case onAttack: {
                            minion.getSpecialItem().addAttack(effect);
                            break;
                        }
                        case onDefend: {
                            minion.getSpecialItem().addDefend(effect);
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean checkVictory() {
        int winner = 999;
        switch (matchType) {
            case kill: {
                checkDeath(players[0].getHero());
                checkDeath(players[1].getHero());
                if (players[0].getHero().isDeath()) winner = 1;
                if (players[1].getHero().isDeath()) winner = 0;
                break;
            }
            case keepFlag: {
                if (players[0].getNum_of_turns_with_flags() >= keepFlagToVictory) winner = 0;
                if (players[1].getNum_of_turns_with_flags() >= keepFlagToVictory) winner = 1;
                break;
            }
            case collectFlag: {
                if (2 * board.numOfFlagsOfPlayer(0, false) > numOfFlags) winner = 0;
                if (2 * board.numOfFlagsOfPlayer(1, false) > numOfFlags) winner = 1;
            }
        }
        if (winner == 999) return false;
        matchResult = new MatchResult(players[0].getUserName(), players[1].getUserName(), winner, age);
        return true;
    }

    private void changeTurn() {
        check_death_of_all();
        players[turn].add_num_of_turns_with_flags(board.numOfFlagsOfPlayer(turn, false));
        turn = 1 - turn;
        age++;
        do_passive_effects(turn);
        board.do_house_effects();
        players[turn].all_works_of_aNewTurn();
        board.all_works_of_aNewTurn(turn);
    }

    public boolean canMove(Minion minion, Location target, boolean printError) {
        if (!minion.canMove(printError)) return false;
        if (!board.canMove(minion, target, printError)) return false;
        return true;
    }

    public boolean canAttack(Minion attacker, Minion defender, boolean printError) {
        if (defender == null) return false;
        if (attacker.getPlyNum() == defender.getPlyNum()) {
            if (printError) MyPrinter.red("it's an insider force!");
            return false;
        }
        if (!attacker.canAttack(printError)) {
            return false;
        }
        if (!board.canAttack(attacker, defender.getLocation())) {
            if (printError) MyPrinter.red("the attacker cant attack from this distance(melee,ranged,hybird)");
            return false;
        }
        if (defender.getSpecialItem() != null &&
                defender.getSpecialItem().getOption() != null &&
                defender.getSpecialItem().getOption().isNot_attack_by_fewer_ap()) {
            if (attacker.get_Real_AttackPower() < defender.get_Real_AttackPower()) {
                if (printError) MyPrinter.red("AP of defender is more than AP of attacker!");
                return false;
            }
        }
        return true;
    }

    public boolean canCounterAttack(Minion attacker, Minion defender, boolean printError) {
        if (!defender.canCounterattack(printError)) {
            return false;
        }
        if (!board.canAttack(defender, attacker.getLocation())) {
            if (printError) MyPrinter.yellow("the defender cant counterAttack from this distance(melee,ranged,hybird)");
            return false;
        }

        return true;
    }

    private void checkDeath(Minion minion) {
        if (minion.getRealHp() <= 0) death(minion);
    }

    private boolean check_correcty_selection(SelectionCellPack selectionCellPack, int playerNum, Location location) {
        Minion minion = board.selectCell(location).getMinion();
        if (minion == null) {
            if (selectionCellPack.getForceType() == ForceType.allCell) return true;
            return false;
        }
        SideType side = selectionCellPack.getSideType();
        ForceType force = selectionCellPack.getForceType();
        int minionSide = minion.getPlyNum();
        if (side == SideType.insider && playerNum != minionSide) return false;
        if (side == SideType.enemy && playerNum == minionSide) return false;
        if (minion.getCardType() == CardType.minion && force == ForceType.hero) return false;
        if (minion.getCardType() == CardType.hero && force == ForceType.minion) return false;
        if (selectionCellPack.getMinionType() == null) return true;
        MinionType minionType = selectionCellPack.getMinionType();
        if (minionType.isRanged() && minion.getMinionTargetsType() == MinionTargetsType.ranged) return true;
        if (minionType.isMelee() && minion.getMinionTargetsType() == MinionTargetsType.melee) return true;
        if (minionType.isHybird() && minion.getMinionTargetsType() == MinionTargetsType.hybird) return true;
        return false;
    }

    private void give_an_effect_to_a_cell(Effect effect, Cell cell) {
        for (HouseEffect houseEffect : effect.getHouseEffects()) {
            cell.addHouseEffect(houseEffect);
        }
    }

    private void give_an_effect_to_a_minion(Effect effect, Minion minion) {
        for (Buff buff : effect.getBuffs()) {
            switch (buff.getType()) {
                case hurt: {
                    minion.hurt(buff.getPower());
                    break;
                }
                case mana: {
                    players[minion.getPlyNum()].addManaBuff(buff);
                    break;
                }
                case fail_positive_buff: {
                    minion.destroy_positive_buffs();
                    break;
                }
                case give_health_to_hero: {
                    int value = minion.getRealHp();
                    players[minion.getPlyNum()].getHero().health_rise(value);
                    death(minion);
                    break;
                }
                case fail_negative_buffs: {
                    minion.destroy_negative_buffs();
                    break;
                }
                case health_rise: {
                    minion.health_rise(buff.getPower());
                    break;
                }
                default: {
                    minion.addBuff(buff);
                    break;
                }
            }
        }
    }

    private void death(Minion minion) {
        if (minion.isDeath()) return;
        minion.death();
        Location location = minion.getLocation();
        ArrayList<Effect> effects = minion.getSpecialItem().getOnDeath();
        give_effects_by_one_location(effects, minion.getPlyNum(), minion.getLocation());
        board.selectCell(minion.getLocation()).minionWent();
        minion.setLocation(null);
        players[minion.getPlyNum()].addGraveyard(minion);
        if (players[minion.getPlyNum()].getSelectedMinion() == minion) {
            players[minion.getPlyNum()].setSelectedMinion(null);
        }
        board.add_flags_to_aCell(minion.getFlags(), location);
    }

    private void collecting_flags_and_items_from_earth(Minion minion, Location location) {
        board.collecting_flags_from_earth(minion, location);
        Item item = board.selectCell(location).getItem();
        if (item == null) return;
        action_item(find_item_minions_target(minion.getPlyNum(), item, location), item);
        board.selectCell(location).setItem(null);
    }

    public void attack(Minion attacker, Minion defender) {
        //checking attack availability...
        if (!canAttack(attacker, defender, true)) return;
        //attack
        int attackPower = attacker.get_Real_AttackPower();
        if (attacker.getSpecialItem() != null &&
                attacker.getSpecialItem().getOption() != null &&
                attacker.getSpecialItem().getOption().isPositiveBuffs_of_Enemy_not_effect()) {
            defender.defence_with_out_holy(attackPower);
        } else {
            defender.defence(attackPower);
        }
        MyPrinter.green(attacker.getCardId() + " attacked " + defender.getCardId() + " successfully.");
        attacker.addNumOfAttacks();
        //specialItem onAttack
        ArrayList<Effect> effects = attacker.getSpecialItem().getOnAttack();
        for (Effect effect : effects) {
            if (effect.getTargetForm() != null) {
                ArrayList<Minion> minions = board.find_all_minions_in_target(attacker.getPlyNum(), defender.getLocation(), effect.getTargetForm());
                for (Minion minion : minions) {
                    give_an_effect_to_a_minion(effect, minion);
                }
            } else {
                give_an_effect_to_a_minion(effect, defender);
            }
        }

        //checking counterAttack availability... and counterAttack
        if (canCounterAttack(attacker, defender, true)) {
            int counterAttackPower = defender.get_Real_AttackPower();
            attacker.hurt(counterAttackPower);
            MyPrinter.green("counterAttack don successfully!");
        }
        //specialItem on defend
        for (Effect effect : defender.getSpecialItem().getOnDefend()) {
            give_an_effect_to_a_minion(effect, defender);
        }
        //check deaths
        checkDeath(attacker);
        checkDeath(defender);
    }

    public void move(Minion minion, Location target) {
        if (!canMove(minion, target, true)) return;
        //move
        board.move(minion, target);
        //collecting flags and items...
        collecting_flags_and_items_from_earth(minion, target);
        MyPrinter.green(minion.getCardId() + " moved to cell " + target.getX() + "," + target.getY() + " successfully!");
    }

    public MatchResult logic() {
        turn = 1;
        while (true) {
            changeTurn();
            if (checkVictory()) return matchResult;
            Player player = this.players[turn];

            boolean flag = false;

            if (player.isHuman()) {
                flag = human_menu(player);
            } else {
                flag = ai_menu(player);
            }
            if (flag) return matchResult;

        }
    }

    private boolean ai_menu(Player player) {
        int playerNum = player.getPlayerNum();
        boolean b = all_available_works(playerNum, false, true);
        if (b) return true;
        return false;
    }

    private boolean human_menu(Player player) {
        int playerNum = player.getPlayerNum();

        while (true) {
            MyPrinter.green("Its turn of " + player.getUserName() + " - players number " + (turn + 1));
            String commandTxt = scanner.nextLine();
            if (commandTxt.equals("show hand") || commandTxt.equals("0")) {
                player.getHand().show();
                continue;
            }
            if (commandTxt.equals("game info") || commandTxt.equals("1")) {
                show_game_info();
                continue;
            }
            if (commandTxt.equals("show my minions") || commandTxt.equals("2")) {
                board.show_all_minions_of(playerNum);
                continue;
            }
            if (commandTxt.equals("show opponent minions") || commandTxt.equals("3")) {
                board.show_all_minions_of(1 - playerNum);
                continue;
            }
            pattern = Pattern.compile("^show card info ([0-9|_|a-z|A-Z]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                String cardId = matcher.group(1);
                Card card = board.find_minion_by_id(cardId);
                if (card != null) {
                    card.showInfo();
                    continue;
                }
                card = player.getHand().find_card_by_id(cardId);
                if (card != null) {
                    card.showInfo();
                    continue;
                }
                card = player.getHand().getNextOne();
                if (card.getCardId().equals(cardId)) {
                    card.showInfo();
                    continue;
                }
                MyPrinter.red("there isn't any card by this id!");
                continue;
            }
            pattern = Pattern.compile("^select ([0-9|_|a-z|A-Z]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                String cardId = matcher.group(1);
                Minion minion = board.find_minion_by_id(cardId);
                if (minion == null) {
                    MyPrinter.red("there isn't any card by this id !");
                    continue;
                }
                if (minion.getPlyNum() != playerNum) {
                    MyPrinter.red("this card is for your opponent!you can't select it.");
                    continue;
                }
                player.setSelectedMinion(minion);
                MyPrinter.green(cardId + " is selected successfully!");
                continue;
            }
            pattern = Pattern.compile("^move to ([0-9]),([0-9])$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                Minion minion = player.getSelectedMinion();
                if (minion == null) {
                    MyPrinter.red("no card has been selected!");
                    continue;
                }
                Location location = string_to_location(matcher.group(1), matcher.group(2));
                if (location == null) continue;
                move(minion, location);
                if (checkVictory()) return true;
                continue;
            }
            pattern = Pattern.compile("^attack ([0-9|_|a-z|A-Z]+)$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                Minion attacker = player.getSelectedMinion();
                if (attacker == null) {
                    MyPrinter.red("no card has been selected!");
                    continue;
                }
                Minion defender = board.find_minion_by_id(matcher.group(1));
                if (defender == null) {
                    MyPrinter.red("there isn't any card by this id!");
                    continue;
                }
                if (defender.getPlyNum() == playerNum) {
                    MyPrinter.red("you cant attack to an insider force!");
                    continue;
                }
                attack(attacker, defender);
                if (checkVictory()) return true;
                continue;
            }
            pattern = Pattern.compile("^combo ([0-9|_|a-z|A-Z]+)( ([0-9|_|a-z|A-Z]+))+$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                //analyze combo request
                String[] strings = commandTxt.split(" ");
                Minion defender = board.find_minion_by_id(strings[1]);
                if (defender == null) {
                    MyPrinter.red(strings[1] + " :there isn't any card by this id!");
                    continue;
                }
                if (defender.getPlyNum() == playerNum) {
                    MyPrinter.red(strings[1] + " is an insider force!you can't choose that.");
                    continue;
                }
                ArrayList<Minion> attackers = new ArrayList<>();
                boolean flag = true;
                for (int i = 2; i < strings.length; i++) {
                    Minion attacker = board.find_minion_by_id(strings[i]);
                    if (attacker == null) {
                        MyPrinter.red(strings[i] + " :there isn't any card by this id!");
                        flag = false;
                        break;
                    }
                    if (attacker.getPlyNum() != playerNum) {
                        MyPrinter.red(strings[i] + " is an enemy force!you can't choose that.");
                        flag = false;
                        break;
                    }
                    if (!attacker.getSpecialItem().isCombo()) {
                        MyPrinter.red(attacker.getName() + " isn't combo!");
                        flag = false;
                        break;
                    }
                    if (!canAttack(attacker, defender, true)) {
                        flag = false;
                        break;
                    }
                    attackers.add(attacker);
                }
                if (flag == true) combo(attackers, defender);
                if (checkVictory()) return true;
                continue;
            }
            pattern = Pattern.compile("^use special power ([0-9]),([0-9])$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                Location location = string_to_location(matcher.group(1), matcher.group(2));
                if (location == null) continue;
                use_special_power(player.getHero(), location);
                if (checkVictory()) return true;
                continue;
            }
            if (commandTxt.equals("show hand")) {
                player.getHand().show();
                continue;
            }
            pattern = Pattern.compile("^insert ([0-9|_|a-z|A-Z]+) in ([0-9]),([0-9])$");
            matcher = pattern.matcher(commandTxt);
            if (matcher.find()) {
                Card card = player.getHand().find_card_by_id(matcher.group(1));
                if (card == null) {
                    MyPrinter.red("there isn't any card by this id!");
                    continue;
                }
                Location location = string_to_location(matcher.group(2), matcher.group(3));
                if (location == null) continue;
                insert(card, location);
                if (checkVictory()) return true;
                continue;
            }
            if (commandTxt.equals("show next card") || commandTxt.equals("11")) {
                player.getHand().showNextOne();
                continue;
            }
            if (commandTxt.equals("enter graveyard") || commandTxt.equals("12")) {
                player.openGraveYard();
                continue;
            }
            if (commandTxt.equals("end turn") || commandTxt.equals("13")) {
                return false;
            }
            if (commandTxt.equals("help in game") || commandTxt.equals("14")) {
                all_available_works(playerNum, true, false);
                continue;
            }
            if (commandTxt.equals("help") || commandTxt.equals("15")) {
                helpMenu();
                continue;
            }
            if (commandTxt.equals("board") || commandTxt.equals("16")) {
                board.showBoard();
                continue;
            }
            if (commandTxt.equals("m")) {
                player.mana_rise(20);
                continue;
            }
            if (commandTxt.equals("play") || commandTxt.equals("17")) {
                if (all_available_works(playerNum, false, true)) return true;
                continue;
            }
            if (commandTxt.equals("board +")) {
                board.board_size_up();
                continue;
            }
            if (commandTxt.equals("board -") || commandTxt.equals("18")) {
                board.board_size_down();
                continue;
            }
            MyPrinter.red("Invalid commandTxt");
        }
    }

    private void helpMenu() {
        MyPrinter.blue("0. show hand");
        MyPrinter.blue("1. game info");
        MyPrinter.blue("2. show my minions");
        MyPrinter.blue("3. show opponent minions");
        MyPrinter.blue("4. show card info <card id>");
        MyPrinter.blue("5. select <card id>");
        MyPrinter.blue("6. move to <x>,<y>");
        MyPrinter.blue("7. attack <card id>");
        MyPrinter.blue("8. combo <opponent card id> <card1> <card2>...");
        MyPrinter.blue("9. use special power <x>,<y>");
        MyPrinter.blue("10. insert <card id> in <x>,<y>");
        MyPrinter.blue("11. show next card");
        MyPrinter.blue("12. enter graveyard");
        MyPrinter.blue("13. end turn");
        MyPrinter.blue("14. help in game");
        MyPrinter.blue("15. help");
        MyPrinter.blue("16. board");
        MyPrinter.blue("17. play");
        MyPrinter.blue("18. board <+|->");
    }

    public void insert(Card card, Location location) {
        if (!canInsert(card, location, true)) return;
        //spell
        if (card instanceof Spell) {
            Spell spell = (Spell) card;
            ArrayList<Effect> effects = spell.getEffects();
            give_effects_by_one_location(effects, spell.getPlyNum(), location);
            players[spell.getPlyNum()].addGraveyard(spell);
        } else { //minion
            Minion minion = (Minion) card;
            board.insert(minion, location);
            SpecialItem specialItem = minion.getSpecialItem();
            if (specialItem != null && specialItem.getOnSpawn().size() != 0) {
                ArrayList<Effect> effects = specialItem.getOnSpawn();
                give_effects_by_one_location(effects, minion.getPlyNum(), location);
            }
            collecting_flags_and_items_from_earth(minion, location);
        }
        card.setSpawn(true);
        players[card.getPlyNum()].mana_use(card.getMana());
        players[card.getPlyNum()].getHand().removeCard(card);
        card.setInserted(true);
        MyPrinter.green(card.getCardId() + " inserted in cell " + location.getX() + "," + location.getY() + " successfully!");
    }

    public boolean canInsert(Card card, Location location, boolean printError) {
        if (card==null)return false;
        if (card.isInserted()) {
            if (printError) MyPrinter.red("this card inserted in past!");
            return false;
        }
        if (players[card.getPlyNum()].getMana() < card.getMana()) {
            if (printError) MyPrinter.red("you haven't enough mana to insert it!");
            return false;
        }
        //spell
        if (card instanceof Spell) {
            Spell spell = (Spell) card;
            if (spell.getSelectionCellPack() != null) {
                if (!check_correcty_selection(spell.getSelectionCellPack(), spell.getPlyNum(), location)) {
                    if (printError) MyPrinter.red("selected cell for effecting spell is incorrect!");
                    return false;
                }
            }
            return true;
        }
        //minion
        Minion minion = (Minion) card;
        if (!board.can_insert_this_minion_at_thisCell(minion, location, printError)) return false;
        return true;
    }

    public boolean canUseSpecialPower(Hero hero, Location target, boolean printError) {
        HeroSpecialItemPack heroPack = hero.getSpecialItem().getHeroPack();
        if (hero.isUsedSpecialItem()) {
            if (printError) MyPrinter.red("you used specialItem in this turn!");
            return false;
        }
        if (heroPack == null) {
            if (printError) MyPrinter.red("special power not exist.");
            return false;
        }
        if (hero.getSpecialItem().getCoolDown().size() == 0) {
            if (printError) MyPrinter.red("special power not exist.");
            return false;
        }
        if (heroPack.getReminded_coolDown() > 0) {
            if (printError) MyPrinter.red("you must wait for " + heroPack.getReminded_coolDown() + " turns!");
            return false;
        }
        if (heroPack.getMana() > players[hero.getPlyNum()].getMana()) {
            if (printError) MyPrinter.red("you have not enough mana to use special power!");
        }
        if (heroPack.getSelectionCellPack() != null) {
            if (!check_correcty_selection(heroPack.getSelectionCellPack(), hero.getPlyNum(), target)) {
                if (printError) MyPrinter.red("selected cell is incorrect!");
                return false;
            }
        }
        return true;
    }

    private void use_special_power(Hero hero, Location location) {
        if (!canUseSpecialPower(hero, location, true)) return;
        //special power
        SelectionCellPack selectionCellPack = hero.getSpecialItem().getHeroPack().getSelectionCellPack();
        if (selectionCellPack == null) location = hero.getLocation();
        ArrayList<Effect> effects = hero.getSpecialItem().getCoolDown();
        give_effects_by_one_location(effects, hero.getPlyNum(), location);
        hero.getSpecialItem().getHeroPack().cooldown_used();
        int mana = hero.getSpecialItem().getHeroPack().getMana();
        players[hero.getPlyNum()].mana_use(mana);
        hero.setUsedSpecialItem(true);
        MyPrinter.green("specialPower of " + hero.getCardId() + " was used successfully!");
    }

    private void combo(ArrayList<Minion> attackers, Minion defender) {
        attack(attackers.get(0), defender);
        for (int i = 1; i < attackers.size(); i++) {
            int attackPower = attackers.get(i).get_Real_AttackPower();
            defender.hurt(attackPower);
        }
        MyPrinter.green("combo was don successfully!");
    }

    private void show_game_info() {
        System.out.println(age + " turns was passed!");
        switch (matchType) {
            case kill: {
                MyPrinter.yellow("players 1: " + players[0].getUserName() + " ,mana : " + players[0].getMana()
                        + " ,health : " + players[0].getHero().getRealHp());
                MyPrinter.yellow("players 2: " + players[1].getUserName() + " ,mana : " + players[1].getMana()
                        + " ,health : " + players[1].getHero().getRealHp());
                return;
            }
            case collectFlag: {
                MyPrinter.yellow("players 1: " + players[0].getUserName() + " ,mana :" + players[0].getMana()
                        + " ,number of flags: " + board.numOfFlagsOfPlayer(0, false));
                MyPrinter.yellow("players 2: " + players[1].getUserName() + " ,mana :" + players[1].getMana()
                        + " ,number of flags: " + board.numOfFlagsOfPlayer(1, false));
                break;
            }
            case keepFlag: {
                MyPrinter.yellow("players 1: " + players[0].getUserName() + " ,mana :" + players[0].getMana()
                        + " ,flag keeping : " + players[0].getNum_of_turns_with_flags());
                MyPrinter.yellow("players 2: " + players[1].getUserName() + " ,mana :" + players[1].getMana()
                        + " ,flag keeping : " + players[1].getNum_of_turns_with_flags());
                break;
            }
        }
        board.numOfFlagsOfPlayer(0, true);
        board.numOfFlagsOfPlayer(1, true);
    }

    private Location string_to_location(String str1, String str2) {
        int x = Integer.valueOf(str1);
        int y = Integer.valueOf(str2);
        if (x > 8 || y > 4) {
            MyPrinter.red("your coordinate isn't in the board!");
            return null;
        }
        return new Location(x, y);
    }

    private void give_effects_by_one_location(ArrayList<Effect> effects, int playerNum, Location location) {
        for (Effect effect : effects) {
            ArrayList<Minion> minions = board.find_all_minions_in_target(playerNum, location, effect.getTargetForm());
            for (Minion minion : minions) {
                give_an_effect_to_a_minion(effect, minion);
            }
        }
        for (Effect effect : effects) {
            ArrayList<Cell> cells = board.find_all_cells_in_target(location, effect.getTargetForm());
            for (Cell cell : cells) {
                give_an_effect_to_a_cell(effect, cell);
            }
        }
    }

    public void do_passive_effects(int playerNum) {
        TargetForm targetForm = new TargetForm(0, 0, Board.width, Board.length, SideType.insider, ForceType.both, null, true);
        ArrayList<Minion> minions = board.find_all_minions_in_target(playerNum, new Location(0, 0), targetForm);
        for (Minion minion : minions) {
            if (minion.getSpecialItem() != null && minion.getSpecialItem().getPassive().size() != 0) {
                give_effects_by_one_location(minion.getSpecialItem().getPassive(), playerNum, minion.getLocation());
            }

        }
    }

    public boolean all_available_works(int playerNum, boolean print, boolean do_works) {
        Location origin = new Location(0, 0);
        TargetForm insiderTargetForm = new TargetForm(0, 0, 9, 5, SideType.insider, ForceType.both, null, true);
        TargetForm enemyTargetForm = new TargetForm(0, 0, 9, 5, SideType.enemy, ForceType.both, null, true);
        ArrayList<Location> allLocations = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                allLocations.add(new Location(i, j));
            }
        }
        ArrayList<Minion> insiderForces = board.find_all_minions_in_target(playerNum, origin, insiderTargetForm);
        ArrayList<Minion> enemyForces = board.find_all_minions_in_target(playerNum, origin, enemyTargetForm);
        //checking moves
        if (do_works) randomSort(allLocations);
        for (Minion minion : insiderForces) {
            for (Location location : allLocations) {
                if (canMove(minion, location, false)) {
                    if (print)
                        MyPrinter.purple(minion.getCardId() + " can move to cell by location : " + location.getX() + "," + location.getY());
                    if (do_works) {
                        if (minion.isDeath()) break;
                        move(minion, location);
                        if (checkVictory()) return true;
                    }
                }
            }
        }
        //checking attacks
        for (Minion attacker : insiderForces) {
            for (Minion defender : enemyForces) {
                if (attacker.isDeath()) break;
                if (defender.isDeath()) break;
                if (canAttack(attacker, defender, false)) {
                    if (print) MyPrinter.purple(attacker.getCardId() + " can attack to " + defender.getCardId() + ".");
                    if (do_works) {
                        attack(attacker, defender);
                        if (checkVictory()) return true;
                    }
                }
            }
        }
        //checking special power
        if (do_works) randomSort(allLocations);
        for (Location location : allLocations) {
            if (canUseSpecialPower(players[playerNum].getHero(), location, false)) {
                if (print)
                    MyPrinter.purple("hero can use special power in cell by location : " + location.getX() + "," + location.getY());
                if (do_works) {
                    Hero hero = players[playerNum].getHero();
                    if (hero.isDeath()) break;
                    use_special_power(hero, location);
                    if (checkVictory()) return true;
                }
            }

        }
        //checking inserting
        if (do_works) randomSort(allLocations);
        ArrayList<Card> handCards = new ArrayList<>();
        handCards.addAll(players[playerNum].getHand().getCards());
        for (Location location : allLocations) {
            for (Card card : handCards) {
                if (canInsert(card, location, false)) {
                    if (print)
                        MyPrinter.purple("you can insert " + card.getCardId() + " in cell by location :" + location.getX() + "," + location.getY());
                    if (do_works) {
                        insert(card, location);
                        if (checkVictory()) return true;
                        break;
                    }
                }
            }
        }

        return false;
    }

    public void check_death_of_all() {
        ArrayList<Minion> minions = board.find_all_minions_in_target(0, new Location(0, 0)
                , new TargetForm(0, 0, 9, 5, SideType.both, ForceType.both, null, true));
        Iterator iterator = minions.iterator();
        while (iterator.hasNext()) {
            Minion minion = (Minion) iterator.next();
            checkDeath(minion);
        }
    }

    private void randomSort(ArrayList<Location> locations) {
        int l = locations.size();
        Random random = new Random();
        for (int i = 0; i < 3 * l; i++) {
            int x = random.nextInt(l);
            int y = random.nextInt(l);
            Collections.swap(locations, x, y);
        }
    }

    public int getBoundOfItems() {
        return boundOfItems;
    }

    public static int getMaxMana() {
        return maxMana;
    }

    public static int getManaEachTurn() {
        return manaEachTurn;
    }

    public static int getKeepFlagToVictory() {
        return keepFlagToVictory;
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static Pattern getPattern() {
        return pattern;
    }

    public static Matcher getMatcher() {
        return matcher;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getTurn() {
        return turn;
    }

    public Board getBoard() {
        return board;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    public int getAge() {
        return age;
    }

    public int getNumOfFlags() {
        return numOfFlags;
    }
}
