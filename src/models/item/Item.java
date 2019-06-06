package models.item;

import models.cards.spell.effect.Effect;
import models.cards.spell.TargetForm;

import java.util.ArrayList;

public class Item {

    private int code;
    private ItemType itemType;
    private int  price;
    private String name;
    private String desc;
    ArrayList<Effect> effects;
    private boolean action; //action or give
    private boolean allCards; //allCards or hero
    private ItemActivateTime activateTime;
    private TargetForm targetForm;

    public Item(int code, ItemType itemType, int price, String name, ArrayList<Effect> effects
            ,boolean action,boolean allCards,ItemActivateTime activateTime,TargetForm targetForm) {
        this.code = code;
        this.itemType = itemType;
        this.price = price;
        this.name = name;
        this.effects = effects;
        this.action=action;
        this.allCards=allCards;
        this.activateTime=activateTime;
        this.targetForm=targetForm;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void show(){
        System.out.println(name);
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAction() {
        return action;
    }

    public boolean isAllCards() {
        return allCards;
    }

    public ItemActivateTime getActivateTime() {
        return activateTime;
    }

    public TargetForm getTargetForm() {
        return targetForm;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
