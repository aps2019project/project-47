package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import runners.Main;

import java.beans.EventHandler;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginRegisterController implements Initializable {

    public JFXTabPane mainPage;
    public JFXTextField userNameField;
    public JFXPasswordField passwordField;
    public JFXButton loginButton;
    public JFXPasswordField newPasswordField;
    public JFXTextField newUserNameField;
    public JFXButton registerButton;
    public JFXButton back;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }


    @FXML
    protected void loginButtonAction(){

    }

    @FXML
    public void registerButtonAction() {
    }

    public void clk_exit(ActionEvent event){
        Main.setScene();
    }
}
