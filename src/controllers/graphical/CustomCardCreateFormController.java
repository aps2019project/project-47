package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.cards.Card;
import models.cards.buff.Buff;
import models.cards.buff.BuffType;
import models.cards.minion.MinionType;
import models.cards.spell.effect.Effect;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomCardCreateFormController implements Initializable {
    public JFXTextField cardName;
    public JFXComboBox<String> typeOfCard;
    public JFXTextField hp;
    public JFXTextField ap;
    public JFXComboBox<String> typeOfAttackType;
    public JFXTextField range;
    public JFXTextField cost;
    public Label titleLabelOfSpecialPowerBox;
    public VBox specialPowerBox;
    public JFXComboBox<String> activationTimeOfSpecialPower;
    public JFXButton addEffect;
    public JFXTextField power;
    public JFXComboBox<String> buffType;
    public JFXTextField start;
    public JFXTextField delay;
    public JFXButton addBuff;
    public JFXTextField X0;
    public JFXTextField Y0;
    public JFXTextField X1;
    public JFXTextField Y1;
    public VBox spellBox;
    public JFXComboBox<String> sideType;
    public JFXComboBox<String> minionType;
    public JFXComboBox<String> forceType;
    public JFXButton addEffectSpell;
    public JFXComboBox<String> activationTimeOfSpecialPowerSpell;
    public JFXButton addBuffSpell;
    public JFXTextField powerSpell;
    public JFXTextField delaySpell;
    public JFXTextField startSpell;
    public JFXComboBox<String> buffTypeSpell;
    public JFXTextField X0Spell;
    public JFXTextField Y0Spell;
    public JFXTextField Y1Spell;
    public JFXTextField X1Spell;
    public JFXComboBox<String> sideTypeSpell;
    public JFXComboBox<String> minionTypeSpell;
    public JFXComboBox<String> forceTypeSpell;
    public JFXCheckBox allOfThemeSpell;
    public JFXCheckBox allOfTheme;
    public ArrayList<Effect> effects = new ArrayList<>();
    public ArrayList<Effect> spellEffects = new ArrayList<>();
    public ArrayList<Buff> buffs = new ArrayList<>();
    public ArrayList<Buff> spellBuffs = new ArrayList<>();
    public Card card;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeOfCard.getItems().addAll("Minion", "Hero", "Spell");
        typeOfAttackType.getItems().addAll("Melee", "Ranged", "Hybrid");
        activationTimeOfSpecialPower.getItems().addAll("On Death", "On Attack", "On Defend", "On Spawn", "Passive");
        activationTimeOfSpecialPowerSpell.getItems().addAll("On Death", "On Attack", "On Defend", "On Spawn", "Passive");
        buffType.getItems().addAll("Holy", "Power", "Poison", "Weakness", "Stun", "Disarm");
        buffTypeSpell.getItems().addAll("Holy", "Power", "Poison", "Weakness", "Stun", "Disarm");
        sideType.getItems().addAll("Insider", "Enemy", "Both");
        sideTypeSpell.getItems().addAll("Insider", "Enemy", "Both");
        forceType.getItems().addAll("Hero", "Minion", "Both", "All Cell");
        forceTypeSpell.getItems().addAll("Hero", "Minion", "Both", "All Cell");
        minionType.getItems().addAll("Melee", "Ranged", "Hybrid");
        minionTypeSpell.getItems().addAll("Melee", "Ranged", "Hybrid");
        specialPowerBox.setVisible(false);
        spellBox.setVisible(false);
    }

    public void changeActiveHomes(ActionEvent actionEvent) {
        switch (typeOfCard.getSelectionModel().getSelectedItem()) {
            case "Minion":
                typeOfAttackType.setDisable(false);
                hp.setDisable(false);
                ap.setDisable(false);
                specialPowerBox.setVisible(true);
                spellBox.setVisible(false);
                titleLabelOfSpecialPowerBox.setText("Create Special Power Of Minion");
                activationTimeOfSpecialPowerSpell.getItems().removeIf(item -> item.equals("Special Item: Cool down"));
                activationTimeOfSpecialPower.getItems().removeIf(item -> item.equals("Special Item: Cool down"));
                break;
            case "Hero":
                typeOfAttackType.setDisable(false);
                hp.setDisable(false);
                ap.setDisable(false);
                specialPowerBox.setVisible(true);
                spellBox.setVisible(false);
                activationTimeOfSpecialPower.getItems().add("Special Item: Cool down");
                activationTimeOfSpecialPowerSpell.getItems().add("Special Item: Cool down");
                titleLabelOfSpecialPowerBox.setText("Create Special Power Of Hero");
                break;
            case "Spell":
                typeOfAttackType.setDisable(true);
                hp.setDisable(true);
                ap.setDisable(true);
                spellBox.setVisible(true);
                specialPowerBox.setVisible(false);
                activationTimeOfSpecialPowerSpell.getItems().removeIf(item -> item.equals("Special Item: Cool down"));
                activationTimeOfSpecialPower.getItems().removeIf(item -> item.equals("Special Item: Cool down"));
                break;
        }
    }

    public void addEffect(ActionEvent actionEvent) {
//        Activation

    }

    public void addEffectSpell(ActionEvent actionEvent) {

    }

    public void addBuff(ActionEvent actionEvent) {
        createBuff(start, power, delay, buffType, buffs);
    }

    public void createCustomCard(ActionEvent actionEvent) {

    }

    public void addBuffSpell(ActionEvent actionEvent) {
        createBuff(startSpell, powerSpell, delaySpell, buffTypeSpell, spellBuffs);
    }

    public void createBuff(JFXTextField startSpell, JFXTextField powerSpell, JFXTextField delaySpell, JFXComboBox<String> buffTypeSpell, ArrayList<Buff> spellBuffs) {
        try {
            int startNum = Integer.parseInt(startSpell.getText());
            int powerNum = Integer.parseInt(powerSpell.getText());
            int delayNum = Integer.parseInt(delaySpell.getText());
            BuffType buffType1 = null;
            switch (buffTypeSpell.getSelectionModel().getSelectedItem()) {
                case "Holy":
                    buffType1 = BuffType.holy;
                    break;
                case "Power":
                    buffType1 = BuffType.attack_power_up;
                    break;
                case "Poison":
                    buffType1 = BuffType.poison;
                    break;
                case "Weakness":
                    buffType1 = BuffType.attack_power_low;
                    break;
                case "Stun":
                    buffType1 = BuffType.stun;
                    break;
                case "Disarm":
                    buffType1 = BuffType.disarm;
                    break;
            }
            Buff buff = new Buff(delayNum, startNum, powerNum, buffType1, false);//??todo?????????????????????????
            spellBuffs.add(buff);
        } catch (Exception ignored) {
        }
    }
}
