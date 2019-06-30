package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.console.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import layouts.AlertHelper;
import models.cards.Card;
import models.cards.buff.Buff;
import models.cards.buff.BuffType;
import models.cards.minion.*;
import models.cards.spell.TargetForm;
import models.cards.spell.effect.Effect;
import runners.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    public ArrayList<Effect> spellEffects = new ArrayList<>();
    public ArrayList<Buff> buffs = new ArrayList<>();
    public ArrayList<Buff> spellBuffs = new ArrayList<>();
    public Card card;
    public SpecialItem specialItem = new SpecialItem(null);
    public SelectionCellPack selectionCellPack = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeOfCard.getItems().addAll("Minion", "Hero", "Spell");
        typeOfAttackType.getItems().addAll("Melee", "Ranged", "Hybrid");
        activationTimeOfSpecialPower.getItems().addAll("On Death", "On Attack", "On Defend", "On Spawn", "Passive");
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
                activationTimeOfSpecialPower.getItems().removeIf(item -> item.equals("Special Item: Cool down"));
                break;
            case "Hero":
                typeOfAttackType.setDisable(false);
                hp.setDisable(false);
                ap.setDisable(false);
                specialPowerBox.setVisible(true);
                spellBox.setVisible(false);
                activationTimeOfSpecialPower.getItems().add("Special Item: Cool down");
                titleLabelOfSpecialPowerBox.setText("Create Special Power Of Hero");
                break;
            case "Spell":
                typeOfAttackType.setDisable(true);
                hp.setDisable(true);
                ap.setDisable(true);
                spellBox.setVisible(true);
                specialPowerBox.setVisible(false);
                activationTimeOfSpecialPower.getItems().removeIf(item -> item.equals("Special Item: Cool down"));
                break;
        }
    }

    public MinionType getMinionType(JFXComboBox<String> minionType) {
        MinionType minionType1 = null;
        switch (minionType.getSelectionModel().getSelectedItem()) {
            case "Melee":
                minionType1 = new MinionType(true, false, false);
                break;
            case "Ranged":
                minionType1 = new MinionType(false, true, false);
                break;
            case "Hybrid":
                minionType1 = new MinionType(false, false, true);
                break;
        }
        return minionType1;
    }

    public SideType getSideType(JFXComboBox<String> sideType) {
        SideType sideType1 = null;
        switch (sideType.getSelectionModel().getSelectedItem()) {
            case "Insider":
                sideType1 = SideType.insider;
                break;
            case "Enemy":
                sideType1 = SideType.enemy;
                break;
            case "Both":
                sideType1 = SideType.both;
                break;
        }
        return sideType1;
    }

    public void addEffectSpell(ActionEvent actionEvent) {
        try {
            SideType sideType1 = getSideType(sideTypeSpell);
            ForceType forceType1 = getForceType(forceTypeSpell);
            MinionType minionType1 = getMinionType(minionTypeSpell);
            boolean allOfTheme = allOfThemeSpell.isSelected();
            System.out.println(allOfTheme);
            TargetForm targetForm = new TargetForm(Integer.parseInt(X0Spell.getText()), Integer.parseInt(Y0Spell.getText()),
                    Integer.parseInt(X1Spell.getText()), Integer.parseInt(Y1Spell.getText()),
                    sideType1, forceType1, minionType1, allOfTheme);
            Effect effect = new Effect(spellBuffs, new ArrayList<>(), targetForm);
            spellBuffs = new ArrayList<>();
            sideTypeSpell.getSelectionModel().clearSelection();
            forceTypeSpell.getSelectionModel().clearSelection();
            minionTypeSpell.getSelectionModel().clearSelection();
            X0Spell.setText("");
            X1Spell.setText("");
            Y0Spell.setText("");
            Y1Spell.setText("");
            AlertHelper.showAlert(Alert.AlertType.INFORMATION , Main.getStage().getOwner() , "Effect Created!" , "Effect Created!");
            spellEffects.add(effect);

        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, Main.getStage().getOwner(), "ERROR", e.getMessage());
        }
    }

    public void addEffect(ActionEvent actionEvent) {
        try {
            SideType sideType1 = getSideType(sideType);
            ForceType forceType1 = getForceType(forceType);
            MinionType minionType1 = getMinionType(minionType);
            boolean allOfTheme1 = allOfTheme.isSelected();
            TargetForm targetForm = new TargetForm(Integer.parseInt(X0.getText()), Integer.parseInt(Y0.getText()),
                    Integer.parseInt(X1.getText()), Integer.parseInt(Y1.getText()),
                    sideType1, forceType1, minionType1, allOfTheme1);
            Effect effect = new Effect(buffs, new ArrayList<>(), targetForm);
            buffs = new ArrayList<>();
            sideType.getSelectionModel().clearSelection();
            forceType.getSelectionModel().clearSelection();
            minionType.getSelectionModel().clearSelection();
            X0.setText("");
            X1.setText("");
            Y0.setText("");
            Y1.setText("");
            AlertHelper.showAlert(Alert.AlertType.INFORMATION , Main.getStage().getOwner() , "Effect Of Special Power Created and Added!" , "Effect Of Special Power Created and Added!");
            switch (activationTimeOfSpecialPower.getSelectionModel().getSelectedItem()) {
                case "On Death":
                    specialItem.addDeath(effect);
                    break;
                case "On Attack":
                    specialItem.addAttack(effect);
                    break;
                case "On Defend":
                    specialItem.addDefend(effect);
                    break;
                case "On Spawn":
                    specialItem.addSpawn(effect);
                    break;
                case "Passive":
                    specialItem.addPassive(effect);
                    break;
            }
            activationTimeOfSpecialPower.getSelectionModel().clearSelection();
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, Main.getStage().getOwner(), "ERROR", Arrays.toString(e.getStackTrace()));
        }
    }

    public ForceType getForceType(JFXComboBox<String> forceTypeSpell) {
        ForceType forceType1 = null;
        switch (forceTypeSpell.getSelectionModel().getSelectedItem()) {
            case "Hero":
                forceType1 = ForceType.hero;
                break;
            case "Minion":
                forceType1 = ForceType.minion;
                break;
            case "Both":
                forceType1 = ForceType.both;
                break;
            case "All Cell":
                forceType1 = ForceType.allCell;
                break;
        }
        return forceType1;
    }

    public void addBuff(ActionEvent actionEvent) {
        Buff buff = createBuff(start, power, delay, buffType);
        buffs.add(buff);
    }

    public void createCustomCard(ActionEvent actionEvent) {

    }

    public void addBuffSpell(ActionEvent actionEvent) {
        Buff buff = createBuff(startSpell, powerSpell, delaySpell, buffTypeSpell);
        spellBuffs.add(buff);
    }

    public Buff createBuff(JFXTextField start, JFXTextField power, JFXTextField delay, JFXComboBox<String> buffType) {
        try {
            int startNum = Integer.parseInt(start.getText());
            int powerNum = Integer.parseInt(power.getText());
            int delayNum = Integer.parseInt(delay.getText());
            BuffType buffType1 = null;
            switch (buffType.getSelectionModel().getSelectedItem()) {
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
            start.setText("");
            power.setText("");
            delay.setText("");
            buffType.getSelectionModel().clearSelection();
            AlertHelper.showAlert(Alert.AlertType.INFORMATION , Main.getStage().getOwner() , "Buff Added!" , "Buff Added!");
            return buff;
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, Main.getStage().getOwner(), "ERROR", e.getMessage());
        }
        return null;
    }

    public void back(ActionEvent actionEvent) {
        Main.getStage().getScene().setRoot(MainMenu.getRoot());
    }
}
