package models.cards.minion;

import models.cards.spell.effect.Effect;
import models.cards.hero.HeroSpecialItemPack;

import java.util.ArrayList;

public class SpecialItem implements Cloneable {
    private ArrayList<Effect> onDeath;
    private ArrayList<Effect> onAttack;
    private ArrayList<Effect> onDefend;
    private ArrayList<Effect> onSpawn;
    private ArrayList<Effect> passive;
    private boolean combo;
    private SpecialOption option;
    //hero
    private ArrayList<Effect> coolDown;
    private HeroSpecialItemPack heroPack;

    public SpecialItem(ArrayList<Effect> onDeath, ArrayList<Effect> onAttack, ArrayList<Effect> onDefend,
                       ArrayList<Effect> onSpawn, ArrayList<Effect> passive, boolean combo,
                       ArrayList<Effect> coolDown, SpecialOption option, HeroSpecialItemPack heroPack) {
        this.onDeath = onDeath;
        this.onAttack = onAttack;
        this.onDefend = onDefend;
        this.onSpawn = onSpawn;
        this.passive = passive;
        this.combo = combo;
        this.coolDown = coolDown;
        this.option = option;
        this.heroPack = heroPack;
    }

    public SpecialItem(SpecialOption option) {
        onAttack = new ArrayList<>();
        onDeath = new ArrayList<>();
        onDefend = new ArrayList<>();
        onSpawn = new ArrayList<>();
        passive = new ArrayList<>();
        coolDown = new ArrayList<>();
        combo = false;
        this.option = option;
        this.heroPack = null;
    }


    public void addCoolDown(Effect effect) {
        coolDown.add(effect);
    }

    public void addDefend(Effect effect) {
        onDefend.add(effect);
    }

    public void addDeath(Effect effect) {
        onDeath.add(effect);
    }

    public void addSpawn(Effect effect) {
        onSpawn.add(effect);
    }

    public void addPassive(Effect effect) {
        passive.add(effect);
    }

    public void addAttack(Effect effect) {
        onAttack.add(effect);
    }

    public void setCombo(boolean combo) {
        this.combo = combo;
    }

    public void setHeroPack(HeroSpecialItemPack heroPack) {
        this.heroPack = heroPack;
    }

    public ArrayList<Effect> getOnDeath() {
        return onDeath;
    }

    public ArrayList<Effect> getOnAttack() {
        return onAttack;
    }

    public ArrayList<Effect> getOnDefend() {
        return onDefend;
    }

    public ArrayList<Effect> getOnSpawn() {
        return onSpawn;
    }

    public ArrayList<Effect> getPassive() {
        return passive;
    }

    public boolean isCombo() {
        return combo;
    }

    public SpecialOption getOption() {
        return option;
    }

    public HeroSpecialItemPack getHeroPack() {
        return heroPack;
    }

    public ArrayList<Effect> getCoolDown() {
        return coolDown;
    }

    @Override
    public SpecialItem clone() throws CloneNotSupportedException {
        HeroSpecialItemPack clonePack;
        if (heroPack == null) {
            clonePack = null;
        } else {
            clonePack = heroPack.clone();
        }
        return new SpecialItem(onDeath, onAttack, onDefend, onSpawn, passive, combo, coolDown, option, clonePack);
    }
}








