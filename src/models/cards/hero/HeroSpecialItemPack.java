package models.cards.hero;

import models.cards.minion.SelectionCellPack;

public class HeroSpecialItemPack implements Cloneable {
    private int mana;
    private int coolDown;
    private int reminded_coolDown;
    private SelectionCellPack selectionCellPack;

    public HeroSpecialItemPack(int mana, int coolDown, SelectionCellPack selectionCellPack) {
        this.mana = mana;
        this.coolDown = coolDown;
        this.reminded_coolDown = coolDown;
        this.selectionCellPack = selectionCellPack;
    }


    public void oneTurnPassed() {
        if (reminded_coolDown > 0) reminded_coolDown--;
    }

    public void cooldown_used() {
        reminded_coolDown = coolDown;
    }

    public int getReminded_coolDown() {
        return reminded_coolDown;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void setReminded_coolDown(int reminded_cooldown) {
        this.reminded_coolDown = reminded_cooldown;
    }

    public SelectionCellPack getSelectionCellPack() {
        return selectionCellPack;
    }

    @Override
    public HeroSpecialItemPack clone() throws CloneNotSupportedException {
        return new HeroSpecialItemPack(mana, coolDown, selectionCellPack);
    }
}
