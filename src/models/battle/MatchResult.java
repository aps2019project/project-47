package models.battle;

public class MatchResult {
    private String user1;
    private String user2;
    private int winner; // true mean's victory and false mean's loose.
    private int gameTime;

    public MatchResult(String user1, String user2, int winner, int gameTime) {
        this.user1 = user1;
        this.user2 = user2;
        this.winner = winner;
        this.gameTime = gameTime;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public int getWinner() {
        return winner;
    }

    public int getGameTime() {
        return gameTime;
    }
}

