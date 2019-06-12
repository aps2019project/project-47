package controllers.graphical;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    public Label playLabel;
    public Label collectionLabel;
    private Bloom bloom = new Bloom();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bloom.setThreshold(0.7);
    }

    public void goToPlayMenu() {

    }

    public void goToCollectionMenu() {

    }

    public void onMouseEnteredOnPlayLabel() {
        playLabel.setEffect(bloom);
    }

    public void onMouseEnteredOnCollectionLabel() {
        collectionLabel.setEffect(bloom);
    }

    public void onMouseExitedFromPlayLabel() {
        playLabel.setEffect(null);
    }

    public void onMouseExitedFromCollectionLabel() {
        collectionLabel.setEffect(null);
    }
}
