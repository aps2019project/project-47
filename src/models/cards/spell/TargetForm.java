package models.cards.spell;

import models.cards.minion.ForceType;
import models.cards.minion.SideType;
import models.cards.minion.MinionType;

public class TargetForm {
    private int x0;
    private int y0;
    private int x1;
    private int y1;
    private MinionType minionType;
    private SideType side;
    private ForceType forceType;
    private boolean allOfTheme; //all of selected target or one of them...

    public TargetForm(int x0,int y0,int x1, int y1, SideType side, ForceType forceType,MinionType minionType,boolean allOfTheme){
        this.x0=x0;
        this.y0=y0;
        this.x1=x1;
        this.y1=y1;
        this.side = side;
        this.forceType = forceType;
        this.allOfTheme=allOfTheme;
        this.minionType=minionType;
    }

    public int getX0() {
        return x0;
    }

    public int getY0() {
        return y0;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public MinionType getMinionType() {
        return minionType;
    }

    public boolean isAllOfTheme() {
        return allOfTheme;
    }
    public SideType getSide() {
        return side;
    }
    public ForceType getForceType() {
        return forceType;
    }
}
