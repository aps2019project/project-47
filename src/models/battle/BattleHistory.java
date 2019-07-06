package models.battle;

import models.battle.board.Board;

import java.util.ArrayList;

public class BattleHistory {
    ArrayList<BattleAction> battleActions;
    Player[] players;
    Board board;

    public BattleHistory(Player[] players, Board board) {
        this.battleActions = new ArrayList<>();
        players = new Player[2];
        players[0] = players[0].clone();
        players[1] = players[1].clone();
        this.board = board.clone();
    }

    public void add(BattleAction battleAction){
        this.battleActions.add(battleAction);
    }

    public BattleAction get(int i){
        return battleActions.get(i);
    }

}
