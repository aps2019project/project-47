package models.battle;

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

}
