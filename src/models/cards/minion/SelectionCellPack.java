package models.cards.minion;

public class SelectionCellPack {
    private SideType sideType;
    private ForceType forceType;
    private MinionType minionType;

    public SelectionCellPack(SideType sideType, ForceType forceType, MinionType minionType) {
        this.sideType = sideType;
        this.forceType = forceType;
        this.minionType = minionType;
    }

    public SideType getSideType() {
        return sideType;
    }

    public ForceType getForceType() {
        return forceType;
    }

    public MinionType getMinionType() {
        return minionType;
    }
}
