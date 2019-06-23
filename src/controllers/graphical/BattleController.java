package controllers.graphical;

import controllers.MyController;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import models.battle.board.Board;

import java.net.URL;
import java.util.ResourceBundle;

public class BattleController extends MyController implements Initializable {
    public GridPane cells;
    private Parent root;

    public void setRoot(Parent root) {
        this.root = root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildGrid();

    }
    public void buildGrid(){
        int width = Board.width;
        int height = Board.length;

        cells.relocate(500,200);
        cells.setStyle("-fx-background-color: green");
        cells.setStyle("-fx-min-height: 10;" +
                "-fx-min-width: 10;" +
                "-fx-border-color: white;" +
                "-fx-border-radius: 5;" +
                "-fx-border-width: 0.5");



        for (int i=0;i<width;i++){
            for (int j = 0; j <height ; j++) {
                Label label=new Label(i+","+j);
                label.setStyle("-fx-background-color: red;" +
                        "-fx-font-size: 30;" +
                        "-fx-fill: white;" );
                label.setPrefSize(100,100);
                cells.add(label,i,j);
            }
        }

        cells.setHgap(20);
        cells.setVgap(20);



    }
}
