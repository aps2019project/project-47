package controllers.graphical;

import com.gilecode.yagson.YaGson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import controllers.Constants;
import controllers.MyController;
import controllers.console.AccountMenu;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import models.Account;
import network.Client;
import network.Requests.account.CreateAccountRequest;
import network.Requests.account.LoginRequest;
import network.Responses.LoginResponse;
import network.Responses.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static controllers.Constants.*;

public class LoginRegisterController extends MyController implements Initializable {

    public JFXTabPane mainPage;
    public JFXTextField userNameField;
    public JFXPasswordField passwordField;
    public JFXButton loginButton;
    public JFXPasswordField newPasswordField;
    public JFXTextField newUserNameField;
    public JFXButton registerButton;
    public Label messageLabelRegister;
    public Label messageLabelLogin;

    public static YaGson yaGson = new YaGson();

    public void loginButtonAction(){
        if (checkFreeBoxes(userNameField, passwordField, messageLabelLogin)) return;
        LoginRequest request = new LoginRequest(userNameField.getText(), passwordField.getText());
        Client.getWriter().println(yaGson.toJson(request));
        Client.getWriter().flush();
    }

    public void login(Response response){
        switch (response.getRequestResult()) {
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
            case ACCOUNT_LOGGED_IN:
                messageLabelLogin.getStyleClass().removeIf(style -> !style.equals("badMessage"));
                messageLabelLogin.getStyleClass().add("badMessage");
                messageLabelLogin.setText("Account " + userNameField.getText() + " Logged in!");
                userNameField.getStyleClass().add("wrong");
                userNameField.setText("");
                passwordField.setText("");
                return;
            case SUCCESSFUL_LOGIN:
                if (response instanceof LoginResponse) {
                    AccountMenu.setLoginAccount(((LoginResponse) response).getAccount());
                    userNameField.setText("");
                    passwordField.setText("");
                    userNameField.getStyleClass().removeIf(style -> style.equals("wrong"));
                    passwordField.getStyleClass().removeIf(style -> style.equals("wrong"));
//                messageLabelLogin.setText("You are logged in successfully!");
                    messageLabelLogin.getStyleClass().removeIf(style -> !style.equals("goodMessage"));
                    messageLabelLogin.getStyleClass().add("goodMessage");
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("../../layouts/mainMenu.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Client.getStage().getScene().setRoot(root);
                }
        }
    }

    public void registerButtonAction() {
        if (checkFreeBoxes(newUserNameField, newPasswordField, messageLabelRegister)) return;
        CreateAccountRequest request = new CreateAccountRequest(newUserNameField.getText(), newPasswordField.getText());
        Client.getWriter().println(yaGson.toJson(request));
        Client.getWriter().flush();
    }

    public void createAccount(Constants requestResult) {
        if (requestResult == ACCOUNT_CREATE_SUCCESSFULLY) {
            messageLabelRegister.setText("The account with name " + newUserNameField.getText() + " created.");
            messageLabelRegister.getStyleClass().removeIf(style -> !style.equals("goodMessage"));
            messageLabelRegister.getStyleClass().add("goodMessage");
            newPasswordField.setText("");
            newUserNameField.setText("");
        } else if (requestResult == ACCOUNT_EXISTS) {
            newUserNameField.getStyleClass().add("wrong");
            messageLabelRegister.getStyleClass().removeIf(style -> !style.equals("badMessage"));
            messageLabelRegister.getStyleClass().add("badMessage");
            messageLabelRegister.setText("The account with name " + newUserNameField.getText() + " existed.");
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
        File file = new File("src/JSONs/Accounts/");
        for (File file1 : file.listFiles()) {
            if (file1.getName().contains(".json")) {
                String json = "";
                try {
                    Scanner scanner = new Scanner(file1);
                    json = scanner.nextLine();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Account account = yaGson.fromJson(json, Account.class);
                AccountMenu.addAccount(account);
            }
        }
        AccountMenu.controller = this;
    }
}
