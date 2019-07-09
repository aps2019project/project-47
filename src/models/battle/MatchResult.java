package models.battle;

public class MatchResult {
    private String user0;
    private String user1;
    private int winner; // true mean's victory and false mean's lose.
    private int gameTime;
    private BattleHistory battleHistory;

    public MatchResult(String user0, String user1, int winner, int gameTime) {
        this.user0 = user0;
        this.user1 = user1;
        this.winner=winner;
        this.gameTime = gameTime;
    }

    public String getUser0() {
        return user0;
    }

    public String getUser1() {
        return user1;
    }

    public int getWinner() {
        return winner;
    }

    public int getGameTime() {
        return gameTime;
    }

    public BattleHistory getBattleHistory() {
        return battleHistory;
    }

    public void setBattleHistory(BattleHistory battleHistory) {
        this.battleHistory = battleHistory;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MatchResult))
            return false;
        MatchResult matchResult = (MatchResult) obj;
        if (!user0.equals(matchResult.user0) || !(user1.equals(matchResult.user1)))
            return false;
        if (gameTime != matchResult.gameTime)
            return false;
        return battleHistory.equals(matchResult.battleHistory);
    }
}

