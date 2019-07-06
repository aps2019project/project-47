package models.battle;

import models.battle.board.Location;

public class BattleAction {
    private String cardId1;
    private String cardId2;
    private Location location;
    private BattleActionType type;

    public BattleAction(String cardId1, String cardId2, Location location, BattleActionType type) {
        this.cardId1 = cardId1;
        this.cardId2 = cardId2;
        this.location = location;
        this.type = type;
    }

    public BattleActionType getType(){
        return type;
    }

    public String getCardId1() {
        return cardId1;
    }

    public String getCardId2() {
        return cardId2;
    }

    public Location getLocation() {
        return location;
    }
}
