package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    public JFXTabPane mainPage;
    public JFXTextField userNameField;
    public JFXPasswordField passwordField;
    public JFXButton registerButton;
    public JFXPasswordField newPasswordField;
    public JFXTextField newUserNameField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected JFXButton loginButton;

    @FXML
    protected void loginButtonAction(){

    }

}
