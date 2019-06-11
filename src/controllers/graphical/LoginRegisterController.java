package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static controllers.console.AccountMenu.doCommand;
import static controllers.console.constants.*;

public class LoginRegisterController implements Initializable {

    public JFXTabPane mainPage;
    public JFXTextField userNameField;
    public JFXPasswordField passwordField;
    public JFXButton loginButton;
    public JFXPasswordField newPasswordField;
    public JFXTextField newUserNameField;
    public JFXButton registerButton;
    public Label messageLabelRegister;
    public Label messageLabelLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    public void loginButtonAction() {
        if (newUserNameField.getText().equals("") | newPasswordField.getText().equals(""))
            return;
    }

    public void registerButtonAction() {
        if (newUserNameField.getText().equals("") | newPasswordField.getText().equals(""))
            return;
        if (doCommand("create account " + newUserNameField.getText() + " " + newPasswordField.getText()) == ACCOUNT_CREATE_SUCCESSFULLY) {
            messageLabelRegister.setText("The account with name " + newUserNameField.getText() + " created.");
            messageLabelRegister.getStyleClass().removeIf(style -> !style.equals("goodMessage"));
            messageLabelRegister.getStyleClass().add("goodMessage");
            newPasswordField.setText("");
            newUserNameField.setText("");
        }
    }

    public void validateUserName() {
        if (doCommand("is" + newUserNameField.getText()) == ACCOUNT_EXISTS) {
            newUserNameField.getStyleClass().add("wrong");
            messageLabelRegister.getStyleClass().removeIf(style -> !style.equals("badMessage"));
            messageLabelRegister.getStyleClass().add("badMessage");
            messageLabelRegister.setText("The account with name " + newUserNameField.getText() + " existed.");
        } else {
            newUserNameField.getStyleClass().removeIf(style -> style.equals("wrong"));
            messageLabelRegister.setText("");
            messageLabelRegister.getStyleClass().removeIf(style -> !style.equals("regularMessage"));
            messageLabelRegister.getStyleClass().add("regularMessage");
        }
    }
}
