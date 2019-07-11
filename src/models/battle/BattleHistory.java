package models.battle;

import com.gilecode.yagson.YaGson;
import models.battle.board.Board;

import java.util.ArrayList;

public class BattleHistory {
    public ArrayList<BattleAction> battleActions;
    Battle battle;


    public BattleHistory(Battle battle) {
        this.battleActions = new ArrayList<>();
        this.battle = battle.clone();
    }

    public void add(BattleAction battleAction){
        this.battleActions.add(battleAction);
    }

    public BattleAction get(int i){
        if (battleActions.size()>i){
            return battleActions.get(i);
        }
        return null;
    }

    public Battle getBattel(){
        return battle;
    }

    public BattleHistory clone(){
        YaGson yaGson  = new YaGson();
        String clonedStr = yaGson.toJson(this);
        return yaGson.fromJson(clonedStr,BattleHistory.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BattleHistory))
            return false;
        BattleHistory battleHistory = (BattleHistory) obj;
        if (battleActions.size() != battleHistory.battleActions.size())
            return false;
        for (int i = 0; i < battleActions.size(); i++) {
            if (!battleActions.get(i).equals(battleHistory.battleActions.get(i)))
                return false;
        }
        return true;
    }
}
