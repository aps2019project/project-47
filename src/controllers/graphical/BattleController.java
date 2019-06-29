package controllers.graphical;

import controllers.MyController;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.battle.Battle;
import models.battle.board.Board;
import models.cards.minion.Minion;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class BattleController extends MyController implements Initializable {
    public AnchorPane anchorPane;
    private GridPane cellGrid;
    public Pane cellsPane;
    private Parent root;
    private Board board;
    private Battle battle;
    private Label[][] cells;


    public Parent getRoot() {
        return root;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
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

    private ImageView creatGif(){
        Image image = new Image(new File("resources/java.gif").toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-background-color: white;" +
                "-fx-opacity: 1");

        //Setting the position of the image
        imageView.setX(0);
        imageView.setY(0);

        //setting the fit height and width of the image view
        imageView.setFitHeight(1000);
        imageView.setFitWidth(1000);

        //Setting the preserve ratio of the image view
        imageView.setPreserveRatio(false);

        return imageView;


    }
    private void buildGrid(){

        GridPane cellGrid=new GridPane();

        int width = Board.width;
        int height = Board.length;
        int boardWidth=800;
        int boardHeight=450;
        int cellGap=5;
        int cellWidth=(boardWidth-(cellGap*(width+1)))/width;
        int cellHeight=(boardHeight-(cellGap*(height+1)))/height;

        cells=new Label[width][height];

        String normal_label_style="-fx-background-color: white;" +
                "-fx-font-size: 30;" +
                "-fx-opacity: 0.2;" +
                "-fx-fill: white;" +
                "-fx-pref-height: " +cellHeight+";"+
                "-fx-pref-width: "+cellWidth;
        String hower_label_style="-fx-background-color: red;" +
                "-fx-font-size: 30;" +
                "-fx-opacity: 0.2;" +
                "-fx-fill: white;" +
                "-fx-pref-height: " +cellHeight+";"+
                "-fx-pref-width: "+cellWidth;
        for (int i=0;i<width;i++){
            for (int j = 0; j <height ; j++) {
                Label label=new Label("");
                label.setStyle(normal_label_style);
                label.setPrefSize(10,10);
                cells[i][j]=label;
                label.setOnMouseEntered(event -> {
                    label.setStyle(hower_label_style);
                });
                label.setOnMouseExited(event -> {
                    label.setStyle(normal_label_style);
                });
                cellGrid.add(label,i,j);
            }
        }

        cellGrid.setHgap(cellGap);
        cellGrid.setVgap(cellGap);
        cellGrid.relocate(0,0);

        Pane perspectivePane = new Pane();
        perspectivePane.getChildren().addAll(cellGrid);

        perspectiveGrid(perspectivePane);
        anchorPane.getChildren().add(perspectivePane);

    }
    private void perspectiveGrid(Pane perspectivePane){
        PerspectiveTransform pt = new PerspectiveTransform();

        pt.setUlx(550);
        pt.setUly(240);
        pt.setUrx(1350);
        pt.setUry(240);
        pt.setLrx(1420);
        pt.setLry(680);
        pt.setLlx(480);
        pt.setLly(680);
        perspectivePane.relocate(550,240);
        //perspectivePane.setEffect(pt);

    }
    private void lbl_click(int i,int j){
        if (battle.getTurn()!=0) {
            return;
        }
        Minion selectedMinion = battle.getPlayers()[0].getSelectedMinion();
        

    }
}
