package models.cards.spell;


import models.cards.Card;
import models.cards.CardType;
import models.cards.minion.SelectionCellPack;
import models.cards.spell.effect.Effect;
import views.MyPrinter;

import java.util.ArrayList;

public class Spell extends Card {
    private ArrayList<Effect> effects;
    private SelectionCellPack selectionCellPack;

    public Spell(int code, String name, int mp, int price, ArrayList<Effect> effects, SelectionCellPack selectionCellPack) {
        super(code, name, mp, price, CardType.spell);
        this.effects = effects;
        this.selectionCellPack = selectionCellPack;
    }

    public SelectionCellPack getSelectionCellPack() {
        return selectionCellPack;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    @Override
    public void show(){
        System.out.println("models.cards.spell.Spell-code: "+code+","+getName());
    }

    @Override
    public Spell clone() throws CloneNotSupportedException {
        ArrayList<Effect> cloneEffects=new ArrayList<>();
        cloneEffects.addAll(effects);
        return new Spell(code,getName(),getMana(),getPrice(),cloneEffects,selectionCellPack);
    }

    @Override
    public void showInfo() {
        MyPrinter.cyan(getName()+","+getMana());
    }
}


