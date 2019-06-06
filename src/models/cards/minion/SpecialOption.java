package models.cards.minion;

public class SpecialOption {
    private boolean notDisarm;
    private boolean notPoison;
    private boolean not_Negative_Buff;
    private boolean not_attack_by_fewer_ap;
    private boolean positiveBuffs_of_Enemy_not_effect;
    private boolean more_ap_by_number_of_least_attacks;
    private boolean notStun;

    private boolean notNegativeBuff;

    public boolean isNotDisarm() {
        return notDisarm;
    }

    public void setNotDisarm(boolean notDisarm) {
        this.notDisarm = notDisarm;
    }

    public boolean isNotPoison() {
        return notPoison;
    }

    public void setNotPoison(boolean notPoison) {
        this.notPoison = notPoison;
    }

    public boolean isNot_Negative_Buff() {
        return not_Negative_Buff;
    }

    public void setNot_Negative_Buff(boolean not_Negative_Buff) {
        this.not_Negative_Buff = not_Negative_Buff;
    }

    public boolean isNot_attack_by_fewer_ap() {
        return not_attack_by_fewer_ap;
    }

    public void setNot_attack_by_fewer_ap(boolean not_attack_by_fewer_ap) {
        this.not_attack_by_fewer_ap = not_attack_by_fewer_ap;
    }

    public boolean isPositiveBuffs_of_Enemy_not_effect() {
        return positiveBuffs_of_Enemy_not_effect;
    }

    public void setPositiveBuffs_of_Enemy_not_effect(boolean positiveBuffs_of_Enemy_not_effect) {
        this.positiveBuffs_of_Enemy_not_effect = positiveBuffs_of_Enemy_not_effect;
    }

    public boolean isMore_ap_by_number_of_least_attacks() {
        return more_ap_by_number_of_least_attacks;
    }

    public void setMore_ap_by_number_of_least_attacks(boolean more_ap_by_number_of_least_attacks) {
        this.more_ap_by_number_of_least_attacks = more_ap_by_number_of_least_attacks;
    }

    public boolean isNotStun() {
        return notStun;
    }

    public void setNotStun(boolean notStun) {
        this.notStun = notStun;
    }

    public boolean isNotNegativeBuff() {
        return notNegativeBuff;
    }

    public void setNotNegativeBuff(boolean notNegativeBuff) {
        this.notNegativeBuff = notNegativeBuff;
    }
}
