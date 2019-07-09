package models.battle.board;

import com.gilecode.yagson.YaGson;
import controllers.MyController;
import controllers.console.MainMenu;
import controllers.graphical.BattleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.cards.minion.SideType;
import models.cards.*;
import models.cards.hero.Hero;
import models.cards.minion.ForceType;
import models.cards.minion.Minion;
import models.cards.minion.MinionTargetsType;
import models.cards.minion.MinionType;
import models.cards.spell.TargetForm;
import models.item.Flag;
import models.item.Item;
import views.MyPrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    public static final Location hero0 = new Location(0, 2);
    public static final Location hero1 = new Location(8, 2);
    private static final Location center = new Location(4, 2);
    public static final int width = 9;
    public static final int height = 5;

    private static Parent root;
    private static BattleController controller;

    public int showBoardSize = 15;

    private Cell[][] cells;

    public Board() {
        cells = new Cell[width][height];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public Cell selectCell(Location location) {
        return cells[location.getX()][location.getY()];
    }

    public Cell selectCell(int x, int y) {
        return cells[x][y];
    }

    public void set_flags_for_random(int number_of_flags) {
        if (number_of_flags == 1) {
            selectCell(center).addFlag(new Flag());
            return;
        }
        Random random = new Random();
        while (number_of_flags > 0) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Location location = new Location(x, y);
            if (location.equals(hero1)) continue;
            if (location.equals(hero0)) continue;
            selectCell(location).addFlag(new Flag());
            number_of_flags--;
        }
    }

    public boolean isThereAPathToMove(Minion minion, Location target) {
        Location from = minion.getLocation();
        int plyNum = minion.getPlyNum();
        if (from.getX() != target.getX() && from.getY() != target.getY()) {
            Minion minion1 = cells[from.getX()][target.getY()].getMinion();
            Minion minion2 = cells[target.getX()][from.getY()].getMinion();
            if (minion1 != null && minion1.getPlyNum() != plyNum
                    && minion2 != null && minion2.getPlyNum() != plyNum) {
                return false;
            }
            return true;
        }
        if (from.distance(target) == 2) {
            Minion minion1;
            if (from.getX() == target.getX()) {
                minion1 = cells[from.getX()][(from.getY() + target.getY()) / 2].getMinion();
            } else {
                minion1 = cells[(from.getX() + target.getX()) / 2][from.getY()].getMinion();
            }
            if (minion1 != null && minion1.getPlyNum() != plyNum) {
                return false;
            }
            return true;
        }
        return true;
    }

    public void all_works_of_aNewTurn(int playerNum) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                Minion minion = cells[i][j].getMinion();
                if (minion != null && minion.getPlyNum() == playerNum) {
                    minion.do_first_Of_every_Turn_works();
                }
            }
        }
    }

    public void show_all_minions_of(int playerNum) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                if (cells[i][j].getMinion() != null) {
                    Cell cell = cells[i][j];
                    Minion minion = cell.getMinion();
                    if (minion.getPlyNum() == playerNum) {
                        MyPrinter.yellow(minion.getCardId() + " in the cell(" + i + "," + j + ")");
                    }
                }
            }
        }
    }

    public Minion find_minion_by_id(String cardId) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                Minion minion = cells[i][j].getMinion();
                if (minion != null && minion.getCardId().equals(cardId)) {
                    return minion;
                }
            }
        }
        return null;
    }

    public boolean canMove(Minion minion, Location target, boolean printError) {
        Location from = minion.getLocation();
        if (minion.getLocation().equals(target)) {
            if (printError) MyPrinter.red("it's his cell!");
            return false;
        }
        Minion m = getMinionByLocation(target);
        if (from.distance(target) > 2) {
            if (printError) MyPrinter.red("distance is bigger than 2! you can't move.");
            return false;
        }
        if (selectCell(target).getMinion() != null) {
            if (printError) MyPrinter.red("this cell is already full!you can't move to this cell.");
            return false;
        }
        if (!this.isThereAPathToMove(minion, target)) {
            if (printError) MyPrinter.red("there isn't any possible path to move!");
            return false;
        }

        return true;
    }

    public void move(Minion minion, Location target) {
        Location from = minion.getLocation();
        selectCell(target).setMinion(minion);
        selectCell(from).minionWent();
        minion.setLocation(target);
        minion.moved();
    }

    public boolean canAttack(Minion attacker, Location target) {
        if (attacker.getMinionTargetsType() == MinionTargetsType.melee) {
            if (!isNear(attacker.getLocation(), target)) return false;
        }
        if (attacker.getMinionTargetsType() == MinionTargetsType.ranged) {
            if (isNear(attacker.getLocation(), target)) return false;
        }
        int attackRange = attacker.getAttackRange();
        Location attackerLocation = attacker.getLocation();
        if (target.distance(attackerLocation) > attackRange) return false;
        return true;
    }

    private boolean isNear(Location location1, Location location2) {
        int xDifferent = Math.abs(location1.getX() - location2.getX());
        int yDifferent = Math.abs(location1.getY() - location2.getY());
        if (xDifferent > 1) return false;
        if (yDifferent > 1) return false;
        if (xDifferent + yDifferent > 2) return false;
        return true;
    }

    public boolean can_insert_this_minion_at_thisCell(Minion minion, Location location, boolean printError) {
        if (selectCell(location).getMinion() != null) {
            if (printError) MyPrinter.red("this cell is full");
            return false;
        }
        TargetForm targetForm = new TargetForm(-1, -1, 2, 2, SideType.insider, ForceType.both, null, true);
        if (find_all_minions_in_target(minion.getPlyNum(), location, targetForm).size() == 0) {
            if (printError) MyPrinter.red("your minion must insert near of another insider force!");
            return false;
        }
        return true;
    }

    public void insert(Minion minion, Location location) {
        selectCell(location).setMinion(minion);
        minion.setLocation(location);
    }

    public int numOfFlagsOfPlayer(int playerNum, boolean toPrint) {
        int sum = 0;
        if (toPrint) MyPrinter.purple("details of player" + (playerNum + 1));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                Minion minion = selectCell(new Location(i, j)).getMinion();
                if (minion == null) continue;
                if (minion.getPlyNum() != playerNum) continue;
                sum += minion.flagSize();
                if (minion.flagSize() > 0 && toPrint) {
                    MyPrinter.purple("force at cell ( " + i + " , " + j + " ) has " + minion.flagSize() + " flag-s.");
                }
            }
        }
        return sum;
    }

    public void add_flags_to_aCell(ArrayList<Flag> flags, Location location) {
        Cell cell = selectCell(location);
        for (Flag flag : flags) {
            cell.addFlag(flag);
        }
    }

    public void show_all_items() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                Cell cell = selectCell(new Location(i, j));
                Item item = cell.getItem();
                if (item != null) {
                    MyPrinter.purple("(" + i + "," + j + "): ");
                    item.show();
                }
                for (Flag flag : cell.getFlags()) {
                    MyPrinter.purple("there is a flag at (" + i + "," + j + ").");
                }
            }
        }
    }

    public void collecting_flags_from_earth(Minion minion, Location location) {
        Cell cell = selectCell(location);
        if (minion == null) return;
        for (Flag flag : cell.getFlags()) {
            minion.addFlag(flag);
        }
        cell.setFlags(new ArrayList<Flag>());
    }

    public ArrayList<Minion> find_all_minions_in_target(int originPlyNum, Location originLocation, TargetForm targetForm) {
        ArrayList<Minion> minions = new ArrayList<>();
        if (targetForm == null) {
            Minion minion = selectCell(originLocation).getMinion();
            if (minion != null) minions.add(minion);
            return minions;
        }
        ArrayList<Cell> cells = find_all_cells_of_target_rectangle(originLocation, targetForm);
        for (Cell cell : cells) {
            Minion minion = cell.getMinion();
            if (minion == null) continue;
            if (targetForm.getSide() == SideType.enemy && minion.getPlyNum() == originPlyNum) continue;
            if (targetForm.getSide() == SideType.insider && minion.getPlyNum() != originPlyNum) continue;
            if (targetForm.getForceType() == ForceType.minion && minion.getCardType() == CardType.hero) continue;
            if (targetForm.getForceType() == ForceType.hero && minion.getCardType() == CardType.minion) continue;
            MinionTargetsType type = minion.getMinionTargetsType();
            MinionType minionType = targetForm.getMinionType();
            if (minionType == null) {
                minions.add(minion);
                continue;
            }
            if (!minionType.isHybird() && type == MinionTargetsType.hybird) continue;
            if (!minionType.isMelee() && type == MinionTargetsType.melee) continue;
            if (!minionType.isRanged() && type == MinionTargetsType.ranged) continue;
            minions.add(minion);
        }
        if (targetForm.isAllOfTheme()) {
            return minions;
        } else {
            Random random = new Random();
            if (minions.size() == 0) return minions;
            Minion minion = minions.get(random.nextInt(minions.size()));
            while (minions.size() > 0) minions.remove(0);
            minions.add(minion);
            return minions;
        }
    }

    public ArrayList<Cell> find_all_cells_in_target(Location originLocation, TargetForm targetForm) {
        if (targetForm == null) {
            ArrayList<Cell> cells = new ArrayList<>();
            cells.add(selectCell(originLocation));
            return cells;
        }
        ArrayList<Cell> cells = find_all_cells_of_target_rectangle(originLocation, targetForm);
        if (targetForm.isAllOfTheme()) {
            return cells;
        } else {
            if (cells.size() == 0) return null;
            Random random = new Random();
            Cell cell = cells.get(random.nextInt(cells.size()));
            while (cells.size() > 0) cells.remove(0);
            cells.add(cell);
            return cells;

        }
    }

    private ArrayList<Cell> find_all_cells_of_target_rectangle(Location originLocation, TargetForm targetForm) {
        ArrayList<Cell> cells = new ArrayList<>();
        if (targetForm == null) {
            cells.add(selectCell(originLocation));
            return cells;
        }
        int x0 = originLocation.getX() + targetForm.getX0();
        int y0 = originLocation.getY() + targetForm.getY0();
        int x1 = originLocation.getX() + targetForm.getX1();
        int y1 = originLocation.getY() + targetForm.getY1();
        for (int i = x0; i < x1; i++) {
            for (int j = y0; j < y1; j++) {
                if (i >= 0 && i < width && j >= 0 && j < height) {
                    cells.add(this.cells[i][j]);
                }
            }
        }
        return cells;

    }

    public void do_house_effects() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = selectCell(i, j);
                cell.actionHouseEffects();
            }
        }
    }

    public void showBoard() {
        int len = width * showBoardSize + width + 1;
        MyPrinter.setBackgroundWhite();
        for (int i = 0; i < len; i++) {
            System.out.print("-");
        }
        MyPrinter.clearBackground();
        System.out.println();
        MyPrinter.setBackgroundWhite();
        for (int i = 0; i < height; i++) {
            MyPrinter.setBackgroundWhite();
            System.out.print("|");
            //location
            for (int j = 0; j < width; j++) {
                MyPrinter.clearBackground();
                Cell cell = selectCell(j, i);
                Minion minion = cell.getMinion();
                String string = "";
                int middle = (showBoardSize / 2) - 2;
                //HP
                if (minion != null) {
                    System.out.print(MyPrinter.ANSI_GREEN);
                    System.out.print(minion.getRealHp());
                    string += minion.getRealHp();
                }
                MyPrinter.clearBackground();
                int count = middle - string.length();
                for (int k = 0; k < count; k++) {
                    string += " ";
                    System.out.print(" ");
                }
                //x:y
                System.out.print(MyPrinter.ANSI_YELLOW);
                string += j + ":" + i;
                System.out.print(j + ":" + i);
                //AP
                count = showBoardSize - string.length() - 2;
                for (int k = 0; k < count; k++) {
                    string += " ";
                    System.out.print(" ");
                }
                if (minion != null) {
                    System.out.print(MyPrinter.ANSI_RED);
                    string += minion.get_Real_AttackPower();
                    System.out.print(minion.get_Real_AttackPower());
                }
                MyPrinter.clearBackground();
                count = showBoardSize - string.length();
                for (int k = 0; k < count; k++) {
                    string += " ";
                    System.out.print(" ");
                }
                MyPrinter.setBackgroundWhite();
                System.out.print("|");
            }
            MyPrinter.clearBackground();
            System.out.println();
            MyPrinter.setBackgroundWhite();
            System.out.print("|");
            //minion name
            for (int j = 0; j < width; j++) {
                MyPrinter.clearBackground();
                Cell cell = selectCell(j, i);
                Minion minion = cell.getMinion();
                String string = "";
                if (minion == null) {
                    for (int k = 0; k < showBoardSize; k++) {
                        string += " ";
                    }
                } else {
                    string += MyPrinter.ANSI_BLUE;
                    string += (minion instanceof Hero ? "h" : "m");
                    string += (minion.getPlyNum() == 0 ? "1" : "2");
                    string += ":";
                    string += MyPrinter.ANSI_RED;
                    int end = Math.min(showBoardSize - 3, minion.getName().length());
                    string += minion.getName().substring(0, end);
                    for (int k = 0; k < showBoardSize - 3 - end; k++) {
                        string += " ";
                    }
                }
                System.out.print(string);
                MyPrinter.setBackgroundWhite();
                System.out.print("|");
            }
            MyPrinter.clearBackground();
            System.out.println();
            MyPrinter.setBackgroundWhite();
            System.out.print("|");
            //continue of minion name
            for (int j = 0; j < width; j++) {
                MyPrinter.clearBackground();
                Cell cell = selectCell(j, i);
                Minion minion = cell.getMinion();
                String string = "";
                if (minion == null) {
                    for (int k = 0; k < showBoardSize; k++) {
                        string += " ";
                    }
                } else {
                    if (minion.getName().length() > showBoardSize - 3) {
                        string += MyPrinter.ANSI_RED;
                        int end = Math.min(2 * showBoardSize - 3, minion.getName().length());
                        string += minion.getName().substring(showBoardSize - 3, end);
                        for (int k = 0; k < 2 * showBoardSize - (end + 3); k++) {
                            string += " ";
                        }
                    } else {
                        for (int k = 0; k < showBoardSize; k++) {
                            string += " ";
                        }
                    }
                }
                System.out.print(string);
                MyPrinter.setBackgroundWhite();
                System.out.print("|");
            }
            MyPrinter.clearBackground();
            System.out.println();
            MyPrinter.setBackgroundWhite();
            System.out.print("|");
            //items and flags
            for (int j = 0; j < width; j++) {
                MyPrinter.clearBackground();
                Cell cell = selectCell(j, i);
                Item item = cell.getItem();
                int f = cell.getFlags().size();
                String string = "";
                if (item == null) {
                    for (int k = 0; k < showBoardSize - 2; k++) {
                        string += " ";
                    }
                } else {
                    string += MyPrinter.ANSI_PURPLE;
                    string += "I";
                    for (int k = 0; k < showBoardSize - 3; k++) {
                        string += " ";
                    }
                }
                if (f == 0) {
                    string += "  ";
                } else {
                    string += MyPrinter.ANSI_CYAN;
                    string += "f";
                    string += MyPrinter.ANSI_RED;
                    string += String.valueOf(f);
                }
                System.out.print(string);
                MyPrinter.setBackgroundWhite();
                System.out.print("|");
            }
            MyPrinter.clearBackground();
            System.out.println();
            MyPrinter.setBackgroundWhite();
            for (int t = 0; t < len; t++) {
                System.out.print("-");
            }
            MyPrinter.clearBackground();
            System.out.println();
        }
        MyPrinter.clearBackground();
        System.out.println();
    }

    public void board_size_up() {
        if (showBoardSize == 15) return;
        showBoardSize++;
    }

    public void board_size_down() {
        if (showBoardSize == 3) return;
        showBoardSize--;
    }

    public static Location getHero0() {
        return hero0;
    }

    public static Location getHero1() {
        return hero1;
    }

    public static Location getCenter() {
        return center;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public int getShowBoardSize() {
        return showBoardSize;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Minion getMinionByLocation(Location location) {
        return cells[location.getX()][location.getY()].getMinion();
    }

    public Minion getMinionById(String minionId){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Minion minion = cells[i][j].getMinion();
                if (minion!=null){
                    if (minion.getCardId().equals(minionId)){
                        return minion;
                    }
                }
            }
        }
        return null;
    }

    public static Parent getRoot() {
        controller = new BattleController();
        return controller.getRoot();
    }

    public static BattleController getController() {
        return controller;
    }
    public Board clone(){
        YaGson yaGson = new YaGson();
        String clonedStr = yaGson.toJson(this);
        Board cloned = yaGson.fromJson(clonedStr, Board.class);
        return cloned;
    }

    public static void setControllerNull(){
        controller = null;
    }
}
