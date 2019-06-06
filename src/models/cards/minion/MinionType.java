package models.cards.minion;

public class MinionType {
    private boolean melee,ranged,hybird;

    public MinionType(boolean melee, boolean ranged, boolean hybird) {
        this.melee = melee;
        this.ranged = ranged;
        this.hybird = hybird;
    }

    public boolean isMelee() {
        return melee;
    }

    public void setMelee(boolean melee) {
        this.melee = melee;
    }

    public boolean isRanged() {
        return ranged;
    }

    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }

    public boolean isHybird() {
        return hybird;
    }

    public void setHybird(boolean hybird) {
        this.hybird = hybird;
    }
}
