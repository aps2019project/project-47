package models.battle.board;

public class Location {
    private int x;
    private int y;
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean equals(Location location){
        if (this.x!=location.getX())return false;
        if (this.y!=location.getY())return false;
        return true;
    }
    public int distance(Location location){
        int x2=location.getX();
        int y2=location.getY();
        int distance = Math.abs(x-x2)+Math.abs(y-y2);
        return distance;
    }
}
