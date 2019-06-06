package models.battle;

public enum MatchType {
    kill,keepFlag,collectFlag;
    public static MatchType convert(int mode){
        if (mode==1)return kill;
        if (mode==2)return collectFlag;
        if (mode==3)return keepFlag;
        return kill;
    }

}
