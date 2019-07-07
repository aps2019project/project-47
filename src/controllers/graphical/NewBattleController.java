package controllers.graphical;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class NewBattleController implements Initializable {

    public static NewBattleController instance;
    {
        instance = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alarmer.start();
    }

    @FXML
    private AnchorPane container;

    @FXML
    private Button rejectButton;

    @FXML
    private Button acceptButton;

    AnimationTimer alarmer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            animation.play();
        }
    };
    final Animation animation = new Transition() {

        {
            setCycleDuration(Duration.millis(6000));
            setInterpolator(Interpolator.EASE_OUT);
        }
        boolean redo = true;
        @Override
        protected void interpolate(double frac) {
            if ((redo && frac > 0.5) || ( !redo && frac < 0.5))
                redo = !redo;
            if (redo) {
                String gradient = "linear-gradient(to right, rgb(26,34,46," + (1 - frac) + "), rgb(54,96,111,"+2*(0.5 - frac) + "))";
                Color vColor = new Color(0, 0, 0, 2*(0.5 - frac));
                container.setStyle("-fx-background-color: " + gradient);
            }
            else {
                String gradient = "linear-gradient(to right, rgb(26,34,46," + (frac) + "), rgb(54,96,111,"+2 * (frac - 0.5) + "))";
                Color vColor = new Color(0, 0, 0,  2*(frac - 0.5));
                container.setStyle("-fx-background-color: " + gradient);
            }
        }
    };

    @FXML
    private void accept(){

    }

    @FXML
    private void reject(){

    }

}
