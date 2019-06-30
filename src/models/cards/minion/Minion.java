package models.cards.minion;

import models.battle.board.Location;
import models.cards.*;
import models.cards.buff.Buff;
import models.cards.buff.BuffType;
import models.item.Flag;
import views.MyPrinter;

import java.util.ArrayList;
import java.util.Iterator;

public class Minion extends Card implements Cloneable {
    private final int maxMove;


    private int Ap;
    private int Hp;
    private Location location;
    private MinionTargetsType minionTargetsType;
    private ArrayList<Buff> positiveBuffs;
    private ArrayList<Buff> negativeBuffs;
    private int numOfAttacks;
    private SpecialItem specialItem;
    private boolean attacked_atThisTurn;
    private boolean moved_atThisTurn;
    private int age;
    private boolean death;
    private ArrayList<Flag> flags;
    private int attackRange;

    public Minion(int code, String name, int mana, int hp, int attackRange, int price, CardType cardType, MinionTargetsType minionTargetsType, int ap, SpecialItem specialItem) {
        super(code, name, mana, price, cardType);
        this.attackRange = attackRange;
        this.Hp = hp;
        this.maxMove = 2;
        Ap = ap;
        this.minionTargetsType = minionTargetsType;
        location = null;
        positiveBuffs = new ArrayList<>();
        negativeBuffs = new ArrayList<>();
        this.numOfAttacks = 0;
        this.specialItem = specialItem;
        if (specialItem == null) {
            this.specialItem = new SpecialItem(null);
        }
        this.cardType = cardType;
        age = 0;
        death = false;
        flags = new ArrayList<>();
    }

    public boolean isDeath() {
        return death;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Minion(code, getName(), getMana(), Hp, attackRange, getPrice(), CardType.minion, minionTargetsType, Ap, (SpecialItem) specialItem.clone());
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void addFlag(Flag flag) {
        flags.add(flag);
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public boolean canMove(boolean printError) {
        if (moved_atThisTurn) {
            if (printError) MyPrinter.red("minion moved at this turn and cant move again!");
            return false;
        }
        if (attacked_atThisTurn) {
            if (printError) MyPrinter.red("minion attacked at this turn and cant move now!");
        }
        return true;
    }

    public void hurt(int value) {
        this.Hp -= value;
    }

    public int getHp() {
        return Hp;
    }

    @Override
    public void show() {
        System.out.println(cardType + "-code: " + code + "," + this.getName() + "," + Ap
                + "," + this.getHp() + "," + getMana());
    }

    public int getAp() {
        return Ap;
    }

    @Override
    public int determineAp() {
        return this.Ap;
    }

    public void destroy_positive_buffs() {
        for (Buff buff : positiveBuffs) {
            if (!buff.isForEver()) positiveBuffs.remove(buff);
        }
    }

    public void destroy_negative_buffs() {
        for (Buff buff : negativeBuffs) {
            if (!buff.isForEver()) {
                negativeBuffs.remove(buff);
            } else {
                if (buff.getStartDelay() == 0) buff.setStartDelay(1);
            }
        }
    }

    public void do_positive_buffs() {
        Iterator iterator = positiveBuffs.iterator();
        while (iterator.hasNext()) {
            Buff buff = (Buff) iterator.next();
            if (buff.getStartDelay() == 0) {
                switch (buff.getType()) {
                    case health_rise: {
                        this.health_rise(buff.getPower());
                        break;
                    }
                }
            }
            if (buff.passOneTurn()) iterator.remove();
        }
    }

    public void do_negative_buffs() {
        Iterator iterator = negativeBuffs.iterator();
        while (iterator.hasNext()) {
            Buff buff = (Buff) iterator.next();
            if (buff.getStartDelay() == 0) {
                switch (buff.getType()) {
                    case poison: {
                        this.hurt(1);
                        break;
                    }
                    case hurtTime: {
                        this.hurt(buff.getPower());
                        break;
                    }
                }
            }
            if (buff.passOneTurn()) iterator.remove();
        }
    }

    public void health_rise(int value) {
        Hp = +value;
    }

    public int getRealHp() {
        int h = getHp();
        for (Buff buff : positiveBuffs) {
            if (buff.getType() == BuffType.health_rise) {
                h += buff.getPower();
            }
        }
        for (Buff buff : negativeBuffs) {
            if (buff.getType() == BuffType.hurt) {
                h -= buff.getPower();
            }
        }
        return h;
    }

    public int determineAP() {
        int a = this.Ap;
        for (Buff buff : positiveBuffs) {
            if (buff.getType() == BuffType.attack_power_up) {
                a += buff.getPower();
            }
        }
        for (Buff buff : negativeBuffs) {
            if (buff.getType() == BuffType.attack_power_low) {
                a -= buff.getPower();
            }
        }
        if (a < 0) return 0;
        return a;
    }

    public boolean isStun() {
        for (Buff buff : negativeBuffs) {
            if (buff.getType() == BuffType.stun) return true;
        }
        return false;
    }

    public int get_Real_AttackPower() {
        int sum = determineAP();
        if (specialItem.getOption() == null) return sum;
        if (specialItem.getOption().isMore_ap_by_number_of_least_attacks()) {
            sum += 5 * numOfAttacks;
        }
        return sum;
    }

    public void addBuff(Buff buff) {
        SpecialOption option = specialItem.getOption();
        if (option != null) {
            switch (buff.getBuffType()) {
                case stun: {
                    if (this.specialItem.getOption().isNotStun()) return;
                    break;
                }
                case disarm: {
                    if (specialItem.getOption().isNotDisarm()) return;
                    break;
                }
                case poison: {
                    if (specialItem.getOption().isNotPoison()) return;
                    break;
                }
            }
        }
        if (buff.getType() == BuffType.holy ||
                buff.getType() == BuffType.attack_power_up ||
                buff.getType() == BuffType.health_rise) {
            positiveBuffs.add(buff);
        } else {
            if (!(option != null && option.isNotNegativeBuff()))
                negativeBuffs.add(buff);
        }
    }

    public void do_first_Of_every_Turn_works() {
        age++;
        do_positive_buffs();
        do_negative_buffs();
        attacked_atThisTurn = false;
        moved_atThisTurn = false;
    }

    public boolean defence(int attack_power) {
        if (specialItem != null && this.specialItem.getOption() != null &&
                this.specialItem.getOption().isNot_attack_by_fewer_ap() && attack_power < get_Real_AttackPower()) {
            return false;
        }
        int holy_value = 0;
        for (Buff buff : positiveBuffs) {
            if (buff.getBuffType() == BuffType.holy) {
                holy_value = +buff.getPower();
            }
        }
        if (holy_value > attack_power) return true;
        attack_power = attack_power - holy_value;
        this.hurt(attack_power);
        return true;
    }

    public boolean defence_with_out_holy(int attack_power) {
        this.hurt(attack_power);
        return true;
    }

    public boolean canAttack(boolean printError) {
        if (age < 1) {
            if (printError) MyPrinter.red("you can't attack by a new inserted minion!");
            return false;
        }
        if (this.get_Real_AttackPower() <= 0) {
            if (printError) MyPrinter.red("attack power of this minion is lower than 0!");
            return false;
        }
        if (this.attacked_atThisTurn) {
            if (printError) MyPrinter.red("you can't attack twice by a minion!");
            return false;
        }
        if (this.isStun()) {
            if (printError) MyPrinter.red("your minion is stun!");
            return false;
        }
        return true;
    }

    public MinionTargetsType getMinionTargetsType() {
        return minionTargetsType;
    }

    public boolean canCounterattack(boolean printError) {
        if (isDisarm()) {
            if (printError) MyPrinter.red("defender is disarm and can't counterAttack!");
            return false;
        }
        if (get_Real_AttackPower() <= 0) {
            if (printError) MyPrinter.red("defender's AP is lower than 0 and can't counterAttack!");
            return false;
        }
        return true;
    }

    public boolean isDisarm() {
        for (Buff buff : negativeBuffs) {
            if (buff.getBuffType() == BuffType.disarm) return true;
        }
        return false;
    }

    public int flagSize() {
        return flags.size();
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void addNumOfAttacks() {
        numOfAttacks++;
    }

    public void moved() {
        this.moved_atThisTurn = true;
    }

    public SpecialItem getSpecialItem() {
        return specialItem;
    }

    public void death() {
        this.death = true;
    }

    @Override
    public void showInfo() {
        MyPrinter.cyan(getName() + "," + minionTargetsType + "," + getRealHp() + "," + get_Real_AttackPower() + "," + getMana());
        show_buffs();
    }

    public void show_buffs() {
        for (Buff buff : negativeBuffs) buff.show();
        for (Buff buff : positiveBuffs) buff.show();
    }

    public ArrayList<Flag> getFlags() {
        return flags;
    }
}
