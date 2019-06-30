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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import models.battle.Battle;
import models.battle.Hand;
import models.battle.board.Board;
import models.cards.minion.Minion;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BattleController extends MyController implements Initializable {
    public AnchorPane anchorPane;
    private GridPane cellGrid;
    public Pane cellsPane;
    private Parent root;
    private Board board;
    private Battle battle;
    private Label[][] cells;
    private HashMap<String,ImageView> imageViewOfCards;
    private CardScene[] cardScenes;


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
        creatHandScene();

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
    private void creatHandScene(){
        int x=500;
        int y=800;
        int width=500;
        int height=200;
        Double multipleOfResizing_cardScene = 0.1;


        Pane parent=new Pane();
        parent.relocate(x,y);
        parent.setPrefSize(width,height);

        HBox hBox=new HBox();
        hBox.setSpacing(30);
        cardScenes=new CardScene[Hand.number_of_cards];

        for (int i = 0; i < Hand.number_of_cards; i++) {
            ImageView imageView=new ImageView(new File("C:\\Users\\asus\\Desktop\\project\\project-47\\src\\resources\\cards\\Mmd_test\\Avalanche_idle.gif").toURI().toString());
            cardScenes[i]=new CardScene();
            cardScenes[i].setCardImageView(imageView);
            hBox.getChildren().add(cardScenes[i].pane);
        }

        parent.getChildren().add(hBox);


        anchorPane.getChildren().add(parent);

    }

    private class CardScene {
        Pane pane;
        public ImageView[] rings;
        public ImageView cardImageView;
        public ImageView mana_view;
        private int ring_width=200;
        private int ring_height=200;

        public CardScene() {


            pane=new Pane();
            pane.getStylesheets().add("C:\\Users\\asus\\Desktop\\project\\project-47\\src\\layouts\\stylesheets\\battlePlane.css");

            pane.setPrefSize(ring_width,ring_height);
            pane.setOnMouseEntered(event -> {
                higher();
            });
            pane.setOnMouseExited(event -> {
                lower();
            });

            rings=new ImageView[2];
            Image ring=new Image(new File("C:\\Users\\asus\\Desktop\\project\\project-47\\src\\resources\\inBattle\\cardSceneInserting\\glow_ring.png").toURI().toString());

            rings[0]=new ImageView(ring);
            rings[0].setFitHeight(ring_height);
            rings[0].setFitWidth(ring_width);
            rings[0].relocate(0,0);

            rings[1]=new ImageView(ring);
            rings[1].setFitHeight(ring_height);
            rings[1].setFitWidth(ring_width);
            rings[1].relocate(0,0);
            rings[1].setRotate(180);

            Image mana_icon=new Image(new File("C:\\Users\\asus\\Desktop\\project\\project-47\\src\\resources\\inBattle\\cardSceneInserting\\icon_mana.png").toURI().toString());
            mana_view=new ImageView(mana_icon);
            mana_view.relocate(ring_width/2.8,ring_height/1.3);

            pane.getChildren().addAll(rings);
            pane.getChildren().add(mana_view);

        }

        public void setCardImageView(ImageView cardImageView) {

            this.cardImageView = cardImageView;
            cardImageView.relocate(5,-20);
            cardImageView.setFitWidth(ring_width);
            cardImageView.setFitHeight(ring_height);
            pane.getChildren().add(cardImageView);
        }
        public void ring_rotate(int degree){
            rings[0].setRotate(degree);
            rings[1].setRotate(degree+180);
        }
        private void higher(){
            ring_rotate(60);

        }
        private void lower(){
            ring_rotate(0);

        }
        private void click(){
            
        }
    }
}
