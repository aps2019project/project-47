package models.cards.hero;

import models.cards.CardType;
import models.cards.minion.Minion;
import models.cards.minion.MinionTargetsType;
import models.cards.minion.SpecialItem;
import views.MyPrinter;

public class Hero extends Minion {
    private boolean usedSpecialItem;
    public Hero(int code, String name, int mp, int hp, int price, int ap, MinionTargetsType minionTargetsType, int attackRange, SpecialItem specialItem) {
        super(code,name,mp,hp,attackRange,price, CardType.hero,minionTargetsType,ap,specialItem);
        this.setCardType(CardType.hero);
        }
    @Override
    public void do_first_Of_every_Turn_works(){
        usedSpecialItem=false;
        super.do_first_Of_every_Turn_works();
        if (getSpecialItem()!=null && getSpecialItem().getHeroPack()!=null){
            getSpecialItem().getHeroPack().oneTurnPassed();
        }
    }
    @Override
    public void show() {
        System.out.println("hero-code: "+code+","+this.getName()+","+this.getAp()+","+this.getHp());
    }
    public void setAttackRange(int range){
        super.setAttackRange(range);
    }

    public boolean isUsedSpecialItem() {
        return usedSpecialItem;
    }

    public void setUsedSpecialItem(boolean usedSpecialItem) {
        this.usedSpecialItem = usedSpecialItem;
    }

    @Override
    public void showInfo() {
        MyPrinter.cyan(getCardType()+","+getName()+","+getMinionTargetsType()+","+getRealHp()+","+get_Real_AttackPower());
        if (this.getSpecialItem()!=null &&
        getSpecialItem().getHeroPack()!=null){
            HeroSpecialItemPack pack=getSpecialItem().getHeroPack();
            MyPrinter.cyan("mana : "+pack.getMana()+" , coolDown : "+pack.getCoolDown()+" and reminded turn to use : "+pack.getReminded_coolDown() );
            show_buffs();
        }
    }

    @Override
    public Hero clone() throws CloneNotSupportedException {
        return new Hero(code,getName(),getMana(),getHp(),getPrice(),getAp()
                ,getMinionTargetsType(),getAttackRange(),getSpecialItem().clone());
    }
}
