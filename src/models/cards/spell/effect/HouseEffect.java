package models.cards.spell.effect;

public class HouseEffect {
    private int power;
    private int time;
    private int startDelay;
    private HouseEffectType type;
    public HouseEffect(HouseEffectType houseEffectType,int hurtValue, int time,int startDelay) {
        this.type=houseEffectType;
        this.power = hurtValue;
        time = time;
        this.startDelay=startDelay;
    }
    public boolean oneTurnPassed(){
        if (startDelay>0){
            startDelay--;
        }else {
            time--;
        }
        if (time==0)return true;
        return false;
    }
    public HouseEffectType getType() {
        return type;
    }
    public void setType(HouseEffectType type) {
        this.type = type;
    }
    public int getTime() {
        return time;
    }
    public int getStartDelay() {
        return startDelay;
    }
}
