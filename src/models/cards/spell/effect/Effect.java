package models.cards.spell.effect;

import models.cards.spell.TargetForm;
import models.cards.buff.Buff;

import java.util.ArrayList;

public class Effect {
    private ArrayList<Buff> buffs;
    private ArrayList<HouseEffect> houseEffects;
    private TargetForm targetForm;

    public Effect(ArrayList<Buff> buffs, ArrayList<HouseEffect> houseEffects, TargetForm targetForm) {
        this.buffs = buffs;
        if (buffs==null)this.buffs=new ArrayList<Buff>();
        this.houseEffects = houseEffects;
        if (houseEffects==null)this.houseEffects=new ArrayList<HouseEffect>();
        this.targetForm = targetForm;
    }
    //getter and setter

    public ArrayList<Buff> getBuffs() {
        return buffs;
    }
    public void setBuffs(ArrayList<Buff> buffs) {
        this.buffs = buffs;
    }
    public ArrayList<HouseEffect> getHouseEffects() {
        return houseEffects;
    }
    public void setHouseEffects(ArrayList<HouseEffect> houseEffects) {
        this.houseEffects = houseEffects;
    }
    public TargetForm getTargetForm() {
        return targetForm;
    }
    public void setTargetForm(TargetForm targetForm) {
        this.targetForm = targetForm;
    }
}
