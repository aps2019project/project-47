package controllers.graphical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import controllers.console.AccountMenu;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import models.Account;
import runners.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

import static controllers.console.AccountMenu.doCommand;
import static controllers.Constants.*;

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

    public static GsonBuilder gsonBuilder;
    public static Gson gson;

    public void loginButtonAction() throws IOException {
        if (checkFreeBoxes(userNameField, passwordField, messageLabelLogin)) return;
        switch (Objects.requireNonNull(doCommand("login " + userNameField.getText() + " " + passwordField.getText()))) {
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
//                messageLabelLogin.setText("You are logged in successfully!");
                messageLabelLogin.getStyleClass().removeIf(style -> !style.equals("goodMessage"));
                messageLabelLogin.getStyleClass().add("goodMessage");
                Parent root = FXMLLoader.load(getClass().getResource("../../layouts/mainMenu.fxml"));
                Main.getStage().getScene().setRoot(root);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        File file = new File("src/JSONs/Accounts/");
        for (File file1 : file.listFiles()){
            if (file1.getName().contains(".json")){
                String json = "";
                try {
                    Scanner scanner = new Scanner(file1);
                    json = scanner.nextLine();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Account account = gson.fromJson(json, Account.class);
                AccountMenu.addAccount(account);
            }
        }
    }
}
