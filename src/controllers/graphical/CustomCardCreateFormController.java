package controllers.graphical;

import com.gilecode.yagson.YaGson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.console.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import layouts.AlertHelper;
import models.cards.Card;
import models.cards.CardType;
import models.cards.GraphicPack;
import models.cards.buff.Buff;
import models.cards.buff.BuffType;
import models.cards.hero.Hero;
import models.cards.minion.*;
import models.cards.spell.Spell;
import models.cards.spell.TargetForm;
import models.cards.spell.effect.Effect;
import models.cards.spell.effect.HouseEffect;
import models.cards.spell.effect.HouseEffectType;
import network.Client;
import network.Requests.shop.CreateCardRequest;
import network.Responses.CreateCardResponse;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class CustomCardCreateFormController implements Initializable {
    public JFXTextField cardName;
    public JFXComboBox<String> typeOfCard;
    public JFXTextField hp;
    public JFXTextField ap;
    public JFXTextField mp;
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
    public JFXTextField X0SpecialPower;
    public JFXTextField Y0SpecialPower;
    public JFXTextField X1SpecialPower;
    public JFXTextField Y1SpecialPower;
    public JFXTextField X0Spell;
    public JFXTextField Y0Spell;
    public JFXTextField Y1Spell;
    public JFXTextField X1Spell;
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
    public JFXComboBox<String> sideTypeSpell;
    public JFXComboBox<String> minionTypeSpell;
    public JFXComboBox<String> forceTypeSpell;
    public JFXCheckBox allOfThemeSpell;
    public JFXCheckBox allOfTheme;
    public JFXCheckBox isHouseEffectSpell;
    public ArrayList<Effect> spellEffectsNormal = new ArrayList<>();
    public ArrayList<HouseEffect> spellEffectsHouse = new ArrayList<>();
    public ArrayList<Buff> buffsSpecialPower = new ArrayList<>();
    public SpecialItem specialItem = new SpecialItem(null);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeOfCard.getItems().addAll("Minion", "Hero", "Spell");
        typeOfAttackType.getItems().addAll("Melee", "Ranged", "Hybrid");
        activationTimeOfSpecialPower.getItems().addAll("On Death", "On Attack", "On Defend", "On Spawn", "Passive");
        buffType.getItems().addAll("Holy", "Power", "Poison", "Weakness", "Stun", "Disarm", "House Poison", "House Fire", "House Holy");
        buffTypeSpell.getItems().addAll("Holy", "Power", "Poison", "Weakness", "Stun", "Disarm", "House Poison", "House Fire", "House Holy");
        sideType.getItems().addAll("Insider", "Enemy", "Both");
        sideTypeSpell.getItems().addAll("Insider", "Enemy", "Both");
        forceType.getItems().addAll("Hero", "Minion", "Both", "All Cell");
        forceTypeSpell.getItems().addAll("Hero", "Minion", "Both", "All Cell");
        minionType.getItems().addAll("Melee", "Ranged", "Hybrid");
        minionTypeSpell.getItems().addAll("Melee", "Ranged", "Hybrid");
        specialPowerBox.setVisible(false);
        spellBox.setVisible(false);
    }

    public void back(ActionEvent actionEvent) {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
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

    public void createCustomCard(ActionEvent actionEvent) {
        try {
            String newCardName = cardName.getText();
            int newCardMp = 0;
            try {
                newCardMp = Integer.parseInt(mp.getText());
            } catch (Exception ignored) {
            }
            int newCardAp = 0;
            try {
                newCardAp = Integer.parseInt(ap.getText());
            } catch (Exception ignored) {
            }
            int newCardHp = 0;
            try {
                Integer.parseInt(hp.getText());
            } catch (Exception ignored){
            }
            int newCardPrice = Integer.parseInt(cost.getText());
            int newCardAttackRange = Integer.parseInt(range.getText());
            MinionTargetsType newCardMinionTargetsType = null;
            switch (typeOfAttackType.getSelectionModel().getSelectedItem()) {
                case "Melee":
                    newCardMinionTargetsType = MinionTargetsType.melee;
                    break;
                case "Ranged":
                    newCardMinionTargetsType = MinionTargetsType.ranged;
                    break;
                case "Hybrid":
                    newCardMinionTargetsType = MinionTargetsType.hybird;
                    break;
            }
            int code = 1000 + new Random().nextInt(1000);
            Card newCard = null;
            switch (typeOfCard.getSelectionModel().getSelectedItem()) {
                case "Hero":
                    newCard = new Hero(code, newCardName, newCardMp, newCardHp, newCardPrice, newCardAp, newCardMinionTargetsType, newCardAttackRange, specialItem);
                    newCard.setGraphicPack(new GraphicPack());
                    newCard.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_archonis_portrait_hex@2x.png");
                    newCard.getGraphicPack().setMovePhotoAddress("src/resources/cards/Mmd_test/Avalanche_run.gif");
                    newCard.getGraphicPack().setAttackPhotoAddress("src/resources/cards/Mmd_test/Avalanche_attack.gif");
                    newCard.getGraphicPack().setBreathingPhotoAddress("src/resources/cards/Mmd_test/Avalanche_breathing.gif");
                    newCard.getGraphicPack().setDeathPhotoAddress("src/resources/cards/Mmd_test/Avalanche_death.gif");
                    newCard.getGraphicPack().setIdlePhotoAddress("src/resources/cards/Mmd_test/Avalanche_idle.gif");
                    break;
                case "Minion":
                    newCard = new Minion(code, newCardName, newCardMp, newCardHp, newCardAttackRange, newCardPrice, CardType.minion, newCardMinionTargetsType, newCardAp, specialItem);
                    newCard.getGraphicPack().setShopPhotoAddress("/resources/cards/boss_archonis_portrait_hex@2x.png");
                    newCard.getGraphicPack().setMovePhotoAddress("src/resources/cards/Mmd_test/Avalanche_run.gif");
                    newCard.getGraphicPack().setAttackPhotoAddress("src/resources/cards/Mmd_test/Avalanche_attack.gif");
                    newCard.getGraphicPack().setBreathingPhotoAddress("src/resources/cards/Mmd_test/Avalanche_breathing.gif");
                    newCard.getGraphicPack().setDeathPhotoAddress("src/resources/cards/Mmd_test/Avalanche_death.gif");
                    newCard.getGraphicPack().setIdlePhotoAddress("src/resources/cards/Mmd_test/Avalanche_idle.gif");
                    break;
                case "Spell": {
                    newCard = new Spell(code, newCardName, newCardMp, newCardPrice, spellEffectsNormal, null);
                    newCard.getGraphicPack().setShopPhotoAddress("/resources/cards/CloneCard.png");
                    newCard.getGraphicPack().setIdlePhotoAddress("src/resources/gifs/allAttack.gif");
                    newCard.getGraphicPack().setSpawnPhotoAddress("src/resources/gifs/allAttack_active.gif");
                    break;
                }
            }
            assert newCard != null;
            newCard.getGraphicPack().setAttackSoundAddress("src/resources/cards/Mmd_test/attackSound.m4a");
            newCard.getGraphicPack().setDeathSoundAddress("src/resources/cards/Mmd_test/deathSound.m4a");
            newCard.getGraphicPack().setMoveSoundAddress("src/resources/cards/Mmd_test/runSound.m4a");
            newCard.getGraphicPack().setSpawnSoundAddress("src/resources/cards/Mmd_test/spawnSound.m4a");
            newCard.getGraphicPack().setImpactSoundAddress("src/resources/cards/Mmd_test/impactSound.m4a");
            newCard.getGraphicPack().setHitSoundAddress("src/resources/cards/Mmd_test/hitSound.m4a");
            newCard.setCode(code);
            if (UniversalShopController.instance == null)
                FXMLLoader.load(getClass().getResource("../../layouts/UniversalShop.fxml"));
            try {
                UniversalShopController.instance.addId(newCard);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            CreateCardRequest createCardRequest = new CreateCardRequest(newCard);
            YaGson yaGson = new YaGson();
            String yaJson1 = yaGson.toJson(createCardRequest);
            Client.getWriter().println(yaJson1);
            Client.getWriter().flush();
            specialItem = new SpecialItem(null);
            mp.setText("");
            ap.setText("");
            hp.setText("");
            cost.setText("");
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, Client.getStage().getOwner(), "WOW!", "Custom Card Created!");
        } catch (Exception e) {
            //AlertHelper.showAlert(Alert.AlertType.ERROR, Client.getStage().getOwner(), "ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private Buff createBuff(JFXTextField startDelay, JFXTextField power, JFXTextField time, JFXComboBox<String> buffType) {
        try {
            int startDelayNum = Integer.parseInt(startDelay.getText());
            int powerNum = Integer.parseInt(power.getText());
            int timeNum = Integer.parseInt(time.getText());
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
            Buff buff = new Buff(startDelayNum, timeNum, powerNum, buffType1, false);
            startDelay.setText("");
            power.setText("");
            time.setText("");
            buffType.getSelectionModel().clearSelection();
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, Client.getStage().getOwner(), "Buff Added!", "Buff Added!");
            return buff;
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, Client.getStage().getOwner(), "ERROR", e.getMessage());
            e.printStackTrace();
        }
        return null;
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

    private TargetForm getTargetFormSpecialPower() {
        SideType sideType1 = getSideType(sideType);
        ForceType forceType1 = getForceType(forceType);
        MinionType minionType1 = getMinionType(minionType);
        boolean allOfTheme1 = allOfTheme.isSelected();
        return getTargetFormSpecialPower(sideType1, forceType1, minionType1, allOfTheme1, X0SpecialPower, Y0SpecialPower, X1SpecialPower, Y1SpecialPower);
    }

    private TargetForm getTargetFormSpecialPower(SideType sideType1, ForceType forceType1, MinionType minionType1, boolean allOfTheme, JFXTextField x0Spell, JFXTextField y0Spell, JFXTextField x1Spell, JFXTextField y1Spell) {
        return new TargetForm(Integer.parseInt(x0Spell.getText()), Integer.parseInt(y0Spell.getText()),
                Integer.parseInt(x1Spell.getText()), Integer.parseInt(y1Spell.getText()),
                sideType1, forceType1, minionType1, allOfTheme);
    }

    public void addEffectSpecialPower(ActionEvent actionEvent) {
        try {
            TargetForm targetForm = getTargetFormSpecialPower();
            ArrayList<Buff> buffs = new ArrayList<>();
            Buff buff = createBuff(start, power, delay, buffType);
            buffs.add(buff);

            Effect effect = new Effect(buffs, null, targetForm);

            buffsSpecialPower = new ArrayList<>();
            X0SpecialPower.setText("");
            X1SpecialPower.setText("");
            Y0SpecialPower.setText("");
            Y1SpecialPower.setText("");
            sideType.getSelectionModel().clearSelection();
            forceType.getSelectionModel().clearSelection();
            minionType.getSelectionModel().clearSelection();
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, Client.getStage().getOwner(), "Effect Of Special Power Created and Added!", "Effect Of Special Power Created and Added!");
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
            AlertHelper.showAlert(Alert.AlertType.ERROR, Client.getStage().getOwner(), "ERROR", Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    public void addEffectSpell(ActionEvent actionEvent) {
        if (isHouseEffectSpell.isSelected()) {
            HouseEffectType type = null;
            switch (buffTypeSpell.getSelectionModel().getSelectedItem()) {
                case "Holy":
                    type = HouseEffectType.holly;
                    break;
                case "Poison":
                    type = HouseEffectType.poison;
                    break;

                case "House Fire":
                    type = HouseEffectType.fire;
                    break;
            }
            HouseEffect houseEffect = new HouseEffect(type,
                    Integer.valueOf(powerSpell.getText()),
                    Integer.valueOf(delaySpell.getText()),
                    Integer.valueOf(startSpell.getText()));
            spellEffectsHouse.add(houseEffect);
            ArrayList<HouseEffect> houseEffects = new ArrayList<>();
            houseEffects.add(houseEffect);
            spellEffectsNormal.add(new Effect(null, houseEffects, getTargetFormSpell()));
            powerSpell.setText("");
            delaySpell.setText("");
            startSpell.setText("");
            buffType.getSelectionModel().clearSelection();
        } else {
            ArrayList<Buff> buffs = new ArrayList<>();
            Buff buff = createBuff(startSpell, powerSpell, delaySpell, buffTypeSpell);
            buffs.add(buff);
            spellEffectsNormal.add(new Effect(buffs, null, getTargetFormSpell()));
        }

    }

    private TargetForm getTargetFormSpell() {
        SideType sideType1 = getSideType(sideTypeSpell);
        ForceType forceType1 = getForceType(forceTypeSpell);
        MinionType minionType1 = getMinionType(minionTypeSpell);
        boolean allOfTheme = allOfThemeSpell.isSelected();
        TargetForm targetForm = getTargetFormSpecialPower(sideType1, forceType1, minionType1, allOfTheme, X0Spell, Y0Spell, X1Spell, Y1Spell);
        X0Spell.setText("");
        X1Spell.setText("");
        Y0Spell.setText("");
        Y1Spell.setText("");
        return targetForm;
    }


}