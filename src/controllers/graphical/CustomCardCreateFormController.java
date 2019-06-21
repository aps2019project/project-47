package controllers.graphical;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomCardCreateFormController  implements Initializable {
    public JFXTextField cardName;
    public JFXComboBox<String> typeOfCard;
    public JFXTextField hp;
    public JFXTextField ap;
    public JFXComboBox<String> typeOfAttackType;
    public JFXTextField range;
    public JFXComboBox<String> specialPower;
    public JFXComboBox<String> specialPowerActivation;
    public JFXComboBox<String> specialPowerCoolDown;
    public JFXTextField cost;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeOfCard.getItems().addAll("Minion", "Hero", "Spell");
        typeOfAttackType.getItems().addAll("Melee" , "Ranged", "Hybrid");
        specialPower.getItems().addAll("qqq");
        specialPowerActivation.getItems().addAll("qqqq");
        specialPowerCoolDown.getItems().addAll("aaaa");
    }

    public void changeActiveHomes(ActionEvent actionEvent) {
        switch (typeOfCard.getSelectionModel().getSelectedItem()){
            case "Minion":
                typeOfAttackType.setDisable(false);
                hp.setDisable(false);
                ap.setDisable(false);
                break;
            case "Hero":
                typeOfAttackType.setDisable(false);
                hp.setDisable(false);
                ap.setDisable(false);
                break;
            case "Spell":
                typeOfAttackType.setDisable(true);
                hp.setDisable(true);
                ap.setDisable(true);
                break;
        }
    }
}
