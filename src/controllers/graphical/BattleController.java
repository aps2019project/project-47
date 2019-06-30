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
import models.battle.board.Cell;
import models.battle.board.Location;
import models.cards.Card;
import models.cards.hero.Hero;
import models.cards.minion.Minion;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BattleController extends MyController implements Initializable {
    private int boardWidth=800;
    private int boardHeight=450;
    private int cellGap=5;



    public AnchorPane anchorPane;
    private GridPane cellGrid;
    public Pane cellsPane;
    private Parent root;
    private Board board;
    private Battle battle;
    private Label[][] cells;
    private CellPane[][] cellPanes;
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
        imageViewOfCards=new HashMap<>();


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
        int cellWidth=(boardWidth-(cellGap*(width+1)))/width;
        int cellHeight=(boardHeight-(cellGap*(height+1)))/height;

        cellPanes = new CellPane[Board.width][Board.length];
        for (int i=0;i<width;i++){
            for (int j = 0; j <height ; j++) {
                cellPanes[i][j]=new CellPane(cellHeight,cellWidth);
                cellGrid.add(cellPanes[i][j].pane,i,j);
            }
        }

        cellGrid.setHgap(cellGap);
        cellGrid.setVgap(cellGap);
        cellGrid.relocate(0,0);

        Pane parentOfCellPanes = new Pane();
        parentOfCellPanes.getChildren().add(cellGrid);

        perspectiveGrid(parentOfCellPanes);
        anchorPane.getChildren().add(parentOfCellPanes);

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
    private void cellPaneClick(int i,int j){

        int turn=battle.getTurn();

        if (!battle.getPlayers()[turn].isHuman()){
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
//            ImageView imageView=new ImageView(new File("src/resources/cards/Mmd_test/Avalanche_idle.gif").toURI().toString());
            cardScenes[i]=new CardScene();
//            cardScenes[i].setCardImageView(imageView);
            hBox.getChildren().add(cardScenes[i].pane);
        }

        parent.getChildren().add(hBox);


        anchorPane.getChildren().add(parent);

    }
    public void initializeBattle(Battle battle){
        this.battle=battle;
        battle.getPlayers()[0].mana_rise(10);
        setHeroOnPlane();
        setHandCards(battle.getPlayers()[0].getHand());
    }
    public void setHeroOnPlane(){
        Hero hero0= battle.getPlayers()[0].getHero();
        ImageView hero0_imageView = new ImageView(new File("resources/cards/Mmd_test/Avalanche_idle.gif").toURI().toString());
        creatImageViewMouseActions(hero0,hero0_imageView);
        cellPanes[0][Board.length/2].addImageView(hero0_imageView);

        Hero hero1= battle.getPlayers()[1].getHero();
        ImageView hero1_imageView = new ImageView(new File("resources/cards/Mmd_test/Avalanche_idle.gif").toURI().toString());
        creatImageViewMouseActions(hero1,hero1_imageView);
        cellPanes[Board.width-1][Board.length/2].addImageView(hero1_imageView);
    }
    public void creatImageViewMouseActions(Card card,ImageView imageView){
        System.out.println(card.getCardId());
        imageViewOfCards.put(card.getCardId(),imageView);
        imageView.setOnMouseClicked(event -> {
            for (int i = 0; i <Board.width ; i++) {
                for (int j = 0; j <Board.length ; j++) {
                    if (battle.canInsert(card,new Location(i,j),false)){
                        cellPanes[i][j].normalLabel();
                    }
                }
            }
            if (!card.isSpawn()){
                for (int i = 0; i <Board.width ; i++) {
                    for (int j = 0; j <Board.length ; j++) {
                        if (battle.canInsert(card,new Location(i,j),false)){
                            cellPanes[i][j].greenLabel();
                        }
                    }
                }
            }else {
                int turn=battle.getTurn();
                if (battle.getPlayers()[turn].isHuman()){
                    battle.getPlayers()[turn].setSelectedMinion((Minion)card);
                    for (int i = 0; i <Board.width ; i++) {
                        for (int j = 0; j <Board.length ; j++) {
                            if (battle.canMove((Minion) card,new Location(i,j),false)){
                                cellPanes[i][j].yellowLabel();
                            }
                            if (battle.canAttack((Minion) card,board.getCells()[i][j].getMinion(),false)){
                                cellPanes[i][j].redLabel();
                            }
                        }
                    }
                }
            }
        });
    }
    public void setHandCards(Hand hand){
        ArrayList<Card> cards=hand.getCards();
        for(int i=0;i<Hand.number_of_cards;i++){
            cardScenes[i].setCardImageView(cards.get(i));
        }
    }
    private class CardScene {
        Pane pane;
        public ImageView[] rings;
        public ImageView cardImageView;
        public ImageView mana_view;
        Label lbl_manaNumbers;
        public int ring_width=200;
        public int ring_height=200;

        public CardScene() {


            pane=new Pane();
            pane.getStylesheets().add("src/layouts/stylesheets/battlePlane.css");

            pane.setPrefSize(ring_width,ring_height);
            pane.setOnMouseEntered(event -> {
                higher();
            });
            pane.setOnMouseExited(event -> {
                lower();
            });

            rings=new ImageView[2];
            Image ring=new Image(new File("src/resources/inBattle/cardSceneInserting/glow_ring.png").toURI().toString());

            rings[0]=new ImageView(ring);
            rings[0].setFitHeight(ring_height);
            rings[0].setFitWidth(ring_width);
            rings[0].relocate(0,0);

            rings[1]=new ImageView(ring);
            rings[1].setFitHeight(ring_height);
            rings[1].setFitWidth(ring_width);
            rings[1].relocate(0,0);
            rings[1].setRotate(180);

            Image mana_icon=new Image(new File("src/resources/inBattle/cardSceneInserting/icon_mana.png").toURI().toString());
            mana_view=new ImageView(mana_icon);
            mana_view.relocate(ring_width/2.8,ring_height/1.3);

            lbl_manaNumbers=new Label("");
            lbl_manaNumbers.getStyleClass().add("lbl_manaNumber");
            lbl_manaNumbers.relocate(ring_width/2.8+21,ring_height/1.3+10);

            pane.getChildren().addAll(rings);
            pane.getChildren().add(mana_view);
            pane.getChildren().add(lbl_manaNumbers);

        }
        public void set_mana_numbers(int manaNumbers){
            lbl_manaNumbers.setText(String.valueOf(manaNumbers));
        }

        public void setCardImageView(Card card) {
            cardImageView = new ImageView(new File("C:\\Users\\asus\\Desktop\\project\\project-47\\src\\resources\\cards\\Mmd_test\\Avalanche_idle.gif").toURI().toString());
            creatImageViewMouseActions(card,cardImageView);
            this.cardImageView = cardImageView;
            cardImageView.relocate(5,-20);
            cardImageView.setFitWidth(ring_width);
            cardImageView.setFitHeight(ring_height);
            set_mana_numbers(card.getMana());
            pane.getChildren().add(cardImageView);
        }
        public void ring_rotate(int degree){
            rings[0].setRotate(degree);
            rings[1].setRotate(degree+180);
        }
        private void higher(){
            ring_rotate(60);
            pane.getStyleClass().add("cardSceneHigher");

        }
        private void lower(){
            ring_rotate(0);

        }
        private void click(){

        }
    }
    private class CellPane {
        int imageViewWidth=150;
        int imageViewHeight=150;
        public Pane pane;
        public Label label;

        public CellPane(int cellHeight,int cellWidth) {
            pane=new Pane();
            pane.getStylesheets().add("layouts/stylesheets/battlePlane.css");
            String normal_label_style="-fx-background-color: white;" +
                    "-fx-font-size: 30;" +
                    "-fx-opacity: 1;" +
                    "-fx-fill: white;" +
                    "-fx-pref-height: " +cellHeight+";" +
                    "-fx-border-color: yellow;" +
                    "-fx-border-width: 0;"+
                    "-fx-pref-width: "+cellWidth;

            String higher_label_style="-fx-background-color: white;" +
                    "-fx-font-size: 30;" +
                    "-fx-opacity: 0.3;" +
                    "-fx-fill: white;" +
                    "-fx-pref-height: " +cellHeight+";" +
                    "-fx-border-color: yellow;" +
                    "-fx-border-width: 5;"+
                    "-fx-pref-width: "+cellWidth;

            pane.setPrefSize(cellWidth,cellHeight);
            pane.setStyle("-fx-alignment: center;");

            label=new Label();
            label.setStyle(normal_label_style);
            label.setPrefSize(cellWidth-2,cellHeight-2);
            pane.getChildren().add(label);

            label.setOnMouseEntered(event -> {
                label.setStyle("-fx-border-width: 5;");
            });
            label.setOnMouseExited(event -> {
                label.setStyle("-fx-border-width: 1");
            });
        }
        public void addImageView(ImageView imageView){
            imageView.setFitWidth(imageViewWidth);
            imageView.setFitHeight(imageViewHeight);
            imageView.relocate(-imageViewWidth/5,-imageViewHeight/3);
            pane.getChildren().add(imageView);

        }
        public void greenLabel(){
            label.setStyle("-fx-background-color: green;" +
                    "-fx-opacity: 0.3");
        }

        public void yellowLabel() {
            label.setStyle("-fx-background-color: yellow;" +
                    "-fx-opacity: 0.3");

        }
        public void redLabel() {
            label.setStyle("-fx-background-color: red;" +
                    "-fx-opacity: 0.3");

        }

        public void normalLabel() {
            label.setStyle("-fx-background-color: white;" +
                    "-fx-opacity: 0.3");
        }
    }
}
