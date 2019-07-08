package controllers.graphical;

import controllers.console.MainMenu;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import network.Client;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreBoardController implements Initializable {

    public VBox board;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void back(MouseEvent mouseEvent) {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }
}
