package models.battle;

public class MatchResult {
    private String user0;
    private String user1;
    private int winner; // true mean's victory and false mean's loose.
    private int gameTime;

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
}

