package models.battle;

import models.battle.board.Board;

import java.util.ArrayList;

public class BattleHistory {
    public ArrayList<BattleAction> battleActions;
    Player[] players;
    Board board;
    MatchType matchType;


    public BattleHistory(Player[] players, Board board,MatchType matchType) {
        this.battleActions = new ArrayList<>();
        this.matchType = matchType;
        this.players = new Player[2];
        this.players[0] = players[0].clone();
        this.players[1] = players[1].clone();
        this.board = board.clone();
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
        players[0].setHuman(false);
        players[1].setHuman(false);
        return new Battle(players,board,matchType);
    }

}
