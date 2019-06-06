package models.cards.buff;

import views.MyPrinter;

public class Buff {
    private BuffType type;
    private int startDelay;
    private int time;
    private int power;
    private boolean forEver;
    private BuffMark mark;
    public Buff(int startDelay, int time, int power,BuffType type,boolean forEver) {
        this.forEver=forEver;
        this.mark=setMark(type);
        this.type=type;
        this.startDelay = startDelay;
        this.time = time;
        this.power = power;
    }
    private BuffMark setMark(BuffType type){
        switch (type) {
            case disarm: return BuffMark.negative;
            case stun: return BuffMark.negative;
            case attack_power_low:return BuffMark.negative;
            case fail_positive_buff:return BuffMark.negative;
            case hurt:return BuffMark.negative;
            case hurtTime:return BuffMark.negative;
            default:return BuffMark.positive;
        }
    }
    public BuffType getBuffType() {
        return type;
    }
    public BuffType getType() {
        return type;
    }
    public int getStartDelay() {
        return startDelay;
    }
    public int getTime() {
        return time;
    }
    public int getPower() {
        return power;
    }
    public boolean passOneTurn(){
        if(startDelay>0){startDelay--; return false;}
        if (time>0){time--; return false;}
        return true;
    }
    public boolean isForEver() {
        return forEver;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }
    public void show (){
        MyPrinter.yellow("buff type : "+type+" , power : "+power+" , time : "+time+" , startDelay : "+startDelay+".");
    }
}
