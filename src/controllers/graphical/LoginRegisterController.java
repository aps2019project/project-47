package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import runners.Main;

import java.io.IOException;

import static controllers.console.AccountMenu.doCommand;
import static controllers.console.Constants.*;

public class LoginRegisterController {

    public JFXTabPane mainPage;
    public JFXTextField userNameField;
    public JFXPasswordField passwordField;
    public JFXButton loginButton;
    public JFXPasswordField newPasswordField;
    public JFXTextField newUserNameField;
    public JFXButton registerButton;
    public Label messageLabelRegister;
    public Label messageLabelLogin;

    public void loginButtonAction() throws IOException {
        if (checkFreeBoxes(userNameField, passwordField, messageLabelLogin)) return;
        switch (doCommand("login " + userNameField.getText() + " " + passwordField.getText())) {
            case INVALID_USERNAME:
                userNameField.getStyleClass().add("wrong");
                userNameField.setText("");
                passwordField.getStyleClass().removeIf(style -> style.equals("wrong"));
                messageLabelLogin.getStyleClass().removeIf(style -> !style.equals("badMessage"));
                messageLabelLogin.getStyleClass().add("badMessage");
                messageLabelLogin.setText("Invalid user name!");
                return;
            case WRONG_PASSWORD:
                passwordField.getStyleClass().add("wrong");
                passwordField.setText("");
                userNameField.getStyleClass().removeIf(style -> style.equals("wrong"));
                messageLabelLogin.getStyleClass().removeIf(style -> !style.equals("badMessage"));
                messageLabelLogin.getStyleClass().add("badMessage");
                messageLabelLogin.setText("Wrong password,try again...");
                return;
            case SUCCESSFUL_LOGIN:
                userNameField.setText("");
                passwordField.setText("");
                userNameField.getStyleClass().removeIf(style -> style.equals("wrong"));
                passwordField.getStyleClass().removeIf(style -> style.equals("wrong"));
                messageLabelLogin.setText("You are logged in successfully!");
                messageLabelLogin.getStyleClass().removeIf(style -> !style.equals("goodMessage"));
                messageLabelLogin.getStyleClass().add("goodMessage");
                Parent root =  FXMLLoader.load(getClass().getResource("../../layouts/mainMenu.fxml"));
                Scene scene = new Scene(root);
                Main.getStage().setScene(scene);
                Main.getStage().setMaximized(true);
                Main.getStage().setFullScreen(true);
        }
    }

    public void registerButtonAction() {
        if (checkFreeBoxes(newUserNameField, newPasswordField, messageLabelRegister)) return;
        if (doCommand("create account " + newUserNameField.getText() + " " + newPasswordField.getText()) == ACCOUNT_CREATE_SUCCESSFULLY) {
            messageLabelRegister.setText("The account with name " + newUserNameField.getText() + " created.");
            messageLabelRegister.getStyleClass().removeIf(style -> !style.equals("goodMessage"));
            messageLabelRegister.getStyleClass().add("goodMessage");
            newPasswordField.setText("");
            newUserNameField.setText("");
        }
    }

    private boolean checkFreeBoxes(JFXTextField userNameField, JFXPasswordField passwordField, Label messageLabel) {
        if (!userNameField.getText().equals(""))
            userNameField.getStyleClass().removeIf(style -> style.equals("wrong"));
        if (!passwordField.getText().equals(""))
            passwordField.getStyleClass().removeIf(style -> style.equals("wrong"));
        if (userNameField.getText().equals("")) {
            userNameField.getStyleClass().add("wrong");
            printFreeBoxMessage(messageLabel);
            return true;
        }
        if (passwordField.getText().equals("")) {
            passwordField.getStyleClass().add("wrong");
            printFreeBoxMessage(messageLabel);
            return true;
        }
        messageLabel.setText("");
        return false;
    }

    private void printFreeBoxMessage(Label messageLabel) {
        messageLabel.getStyleClass().removeIf(style -> !style.equals("badMessage"));
        messageLabel.getStyleClass().add("badMessage");
        messageLabel.setText("Enter free fields!");
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

    public void typeOnUserNameField() {
        userNameField.getStyleClass().removeIf(style -> style.equals("wrong"));
        messageLabelLogin.setText("");
    }

    public void typeOnPasswordField() {
        passwordField.getStyleClass().removeIf(style -> style.equals("wrong"));
        messageLabelLogin.setText("");
    }

    public void typeOnNewPasswordField() {
        newPasswordField.getStyleClass().removeIf(style -> style.equals("wrong"));
        messageLabelRegister.setText("");
    }
}
