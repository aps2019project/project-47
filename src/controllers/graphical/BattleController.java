package controllers.graphical;

import controllers.MyController;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import models.battle.Battle;
import models.battle.board.Board;

import java.net.URL;
import java.util.ResourceBundle;

public class BattleController extends MyController implements Initializable {
    public GridPane cellGrid;
    private Parent root;
    private Board board;
    private Battle battle;
    private Label[][] cells;


    public Parent getRoot() {
        return root;
    }

    public Board getBoard() {
        return board;
    }

    public Battle getBattle() {
        return battle;
    }

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
        int boardWidth=800;
        int boardHeight=450;
        int cellGap=5;
        int cellWidth=(boardWidth-(cellGap*(width+1)))/width;
        int cellHeight=(boardHeight-(cellGap*(height+1)))/height;

        cells=new Label[width][height];

        for (int i=0;i<width;i++){
            for (int j = 0; j <height ; j++) {
                Label label=new Label("");
                label.setStyle("-fx-background-color: white;" +
                        "-fx-font-size: 30;" +
                        "-fx-opacity: 0.1;" +
                        "-fx-fill: white;" +
                        "-fx-pref-height: " +cellHeight+";"+
                        "-fx-pref-width: "+cellWidth);
                label.setPrefSize(10,10);
                cells[i][j]=label;
                cellGrid.add(label,i,j);
            }
        }

        cellGrid.setHgap(cellGap);
        cellGrid.setVgap(cellGap);
        cellGrid.relocate(550,240);
        cellGrid.setStyle("-fx-background-color: green");
        cellGrid.setStyle("-fx-min-height: 10;" +
                "-fx-min-width: 10");
    }
}
