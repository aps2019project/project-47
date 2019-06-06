package models.battle.board;

import models.cards.spell.effect.HouseEffect;
import models.cards.buff.Buff;
import models.cards.buff.BuffType;
import models.cards.minion.Minion;
import models.item.Flag;
import models.item.Item;

import java.util.ArrayList;

public class Cell {
    private Minion minion;
    private Item item;
    private ArrayList<HouseEffect> houseEffects;
    private ArrayList<Flag> flags;

    public Cell() {
        minion = null;
        item=null;
        houseEffects = new ArrayList<HouseEffect>();
        flags=new ArrayList<>();
    }
    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }
    public void setFlags(ArrayList<Flag> flags) {
        this.flags = flags;
    }
    public void addFlag(Flag flag){
        flags.add(flag);
    }
    public ArrayList<Flag> getFlags() {
        return flags;
    }
    public Minion getMinion() {
        return minion;
    }
    public boolean setMinion(Minion minion) {
        if (this.minion != null) {
            System.out.printf("there is already a card in this cell, try another cell.");
            return false;
        }
        this.minion=minion;
        return true;
    }
    public boolean minionWent() {
        if (minion == null) return false;
        minion = null;
        return true;
    }
    public boolean isThereThisMinion(String cardId) {
        if (this.minion.getCardId().equals(cardId)) return true;
        return false;
    }
    public void addHouseEffect(HouseEffect effect) {
        houseEffects.add(effect);
    }
    public void actionHouseEffects(){
        if (minion==null)return;
        for (HouseEffect houseEffect:houseEffects){
            if (houseEffect.getStartDelay()!=0)continue;
            switch (houseEffect.getType()){
                case fire:{
                    minion.hurt(1);
                    return;
                }
                case holly:{
                    minion.addBuff(new Buff(0,1,1, BuffType.holy,false));
                    return;
                }
                case poison:{
                    minion.addBuff(new Buff(0,3,1,BuffType.poison,false));
                }
                if (houseEffect.oneTurnPassed())houseEffects.remove(houseEffect);
            }
        }
    }
}
