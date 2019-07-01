package controllers.graphical;

import controllers.MyController;
import javafx.animation.TranslateTransition;
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
import javafx.scene.shape.Circle;
import models.battle.Battle;
import models.battle.Hand;
import models.battle.Player;
import models.battle.board.Board;
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

    public AnchorPane anchorPane;
    private GridPane cellGrid;
    public Pane cellsPane;
    private Parent root;
    private Board board;
    private Battle battle;
    private HashMap<String, ImageView> imageViewOfCards;
    private Card[] playerSelectedCard;
    private GraphicalHand graphicalHand;
    private GraphicalBoard graphicalBoard;
    private StateOfMouseClicked[] stateOfMouseClickeds;
    private int turn;
    private Player[] players;

    public void setRoot(Parent root) {
        this.root = root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageViewOfCards = new HashMap<>();
        creatBoardCells();
        creatHandScene();

    }

    public void freeClick(){
        System.out.println("free click");
        stateOfMouseClickeds[turn] = StateOfMouseClicked.free;
        playerSelectedCard[turn] = null;
        graphicalBoard.allCellsNormal();
    }

    private void creatBoardCells() {
        graphicalBoard = new GraphicalBoard();
        anchorPane.getChildren().add(graphicalBoard.parentPane);
    }

    private void creatHandScene() {
        graphicalHand = new GraphicalHand();
        anchorPane.getChildren().add(graphicalHand.parentPane);
    }

    public void initializeBattle(Battle battle) {
        this.battle = battle;
        this.board = battle.getBoard();
        playerSelectedCard = new Card[2];
        battle.getPlayers()[0].mana_rise(10);
        setHeroOnPlane_atStatingBattle();
        graphicalHand.setHand(battle.getPlayers()[0].getHand());
        graphicalBoard.setBoard(board);
        graphicalHand.updateHand();
        stateOfMouseClickeds = new StateOfMouseClicked[2];
        stateOfMouseClickeds[0]=StateOfMouseClicked.free;
        stateOfMouseClickeds[1]=StateOfMouseClicked.free;
        turn=battle.getTurn();
        players = battle.getPlayers();
    }

    public void setHeroOnPlane_atStatingBattle() {

        Hero hero0 = battle.getPlayers()[0].getHero();
        graphicalBoard.setCardInCell(hero0, new Location(0, Board.length / 2));

        Hero hero1 = battle.getPlayers()[1].getHero();
        graphicalBoard.setCardInCell(hero1, new Location(Board.width - 1, Board.length / 2));

    }

    private void insert(Card card, Location location) {
        graphicalHand.insertCard(card);

    }

    private void attack(Minion attacker, Minion defender) {

    }

    public void move(Minion minion, Location target) {
        Location firstLocation = minion.getLocation();
        TranslateTransition translateTransition = new TranslateTransition();

    }

    private class CardScene {
        Pane parentPane;
        public ImageView ring;
        public ImageView cardImageView;
        public ImageView mana_view;
        Label lbl_manaNumbers;
        public int paneWidth = 200;
        public int paneHeight = 200;
        Circle downerCircle;
        Circle upperCircle;
        Card card;
        int numberOfCardScene;

        public CardScene(int numberOfCardScene) {

            this.numberOfCardScene=numberOfCardScene;

            parentPane = new Pane();
            parentPane.getStylesheets().add("layouts/stylesheets/battle/cardScene.css");
            parentPane.setPrefSize(paneWidth, paneHeight);

            downerCircle = new Circle();
            downerCircle.setRadius(Math.min(paneWidth, paneHeight) / 3.5);
            downerCircle.relocate(paneHeight / 5, paneWidth / 5);
            downerCircle.getStyleClass().add("downerCircle_firstStyle");
            downerCircle.getStyleClass().add("downerCircle_lower");
            parentPane.getChildren().add(downerCircle);

            ring = new ImageView();
            Image ring_image = new Image(new File("src/resources/inBattle/cardSceneInserting/ring_glow_for_cardScene.png").toURI().toString());
            ring = new ImageView(ring_image);
            ring.setFitHeight(paneHeight);
            ring.setFitWidth(paneWidth);
            ring.relocate(0, 0);

            upperCircle = new Circle();
            upperCircle.setRadius(Math.min(paneWidth, paneHeight) / 3.5);
            upperCircle.relocate(paneHeight / 5, paneWidth / 5);
            upperCircle.getStyleClass().add("upperCircle_firstStyle");
            upperCircle.setOnMouseEntered(event -> {
                higher();
            });
            upperCircle.setOnMouseExited(event -> {
                lower();
            });
            upperCircle.setOnMouseClicked(event -> {
                click();
            });
            parentPane.getChildren().add(upperCircle);

            Image mana_icon = new Image(new File("src/resources/inBattle/cardSceneInserting/icon_mana.png").toURI().toString());
            mana_view = new ImageView(mana_icon);
            mana_view.relocate(paneWidth / 2.8, paneHeight / 1.3);

            lbl_manaNumbers = new Label();
            lbl_manaNumbers.getStyleClass().add("lbl_manaNumber");
            lbl_manaNumbers.relocate(paneWidth / 2.8 + 21, paneHeight / 1.3 + 10);

            parentPane.getChildren().add(ring);
            parentPane.getChildren().add(mana_view);
            parentPane.getChildren().add(lbl_manaNumbers);

        }

        public void set_mana_numbers(int manaNumbers) {
            lbl_manaNumbers.setText(String.valueOf(manaNumbers));
        }

        public void setCardImageView(Card card) {
            this.card = card;
            ImageView cardImageView = new ImageView(new File(card.getGraphicPack().getIdlePhotoAddress()).toURI().toString());
            this.cardImageView = cardImageView;
            cardImageView.relocate(0, -paneHeight / 10);
            cardImageView.setFitWidth(paneWidth);
            cardImageView.setFitHeight(paneHeight);
            set_mana_numbers(card.getMana());
            parentPane.getChildren().remove(upperCircle);
            parentPane.getChildren().add(cardImageView);
            parentPane.getChildren().add(upperCircle);
        }

        public void ring_rotate(int degree) {
            ring.setRotate(degree);
        }

        private void higher() {
            ring_rotate(60);
            downerCircle.getStyleClass().remove("downerCircle_lower");
            downerCircle.getStyleClass().add("downerCircle_higher");

        }

        private void lower() {
            ring_rotate(0);
            downerCircle.getStyleClass().remove("downerCircle_higher");
            downerCircle.getStyleClass().add("downerCircle_lower");
        }

        private void click() {
            if (card==null){
                freeClick();
            }
            if (!players[turn].isHuman() ){
                stateOfMouseClickeds[1-turn] = null;
            }
            playerSelectedCard[turn] = card;
            stateOfMouseClickeds[turn] = StateOfMouseClicked.insertingCardClicked;
            graphicalBoard.show_available_cells_for_insert(card);
        }

        public void removeCard() {
            parentPane.getChildren().remove(cardImageView);
            cardImageView=null;
        }

        public Card getCard() {
            return card;
        }
    }

    private class CellPane {
        int imageViewWidth = 150;
        int imageViewHeight = 150;
        public Pane pane;
        public Label downerLabel;
        Label upperLabel;

        public CellPane(int cellHeight, int cellWidth, int i, int j) {


            pane = new Pane();
            pane.getStylesheets().add("layouts/stylesheets/battle/cellPane.css");
            pane.setPrefSize(cellWidth, cellHeight);

            downerLabel = new Label();
            downerLabel.setPrefSize(cellWidth - 2, cellHeight - 2);
            downerLabel.getStyleClass().add("downerLabel_atFirst");
            downerLabel.getStyleClass().add("downerLabel_white");
            downerLabel.getStyleClass().add("downerLabel_lower");
            pane.getChildren().add(downerLabel);

            upperLabel = new Label();
            upperLabel.getStyleClass().add("upperLabel_atFirst");
            upperLabel.setPrefSize(cellWidth - 2, cellHeight - 2);
            pane.getChildren().add(upperLabel);

            upperLabel.setOnMouseEntered(event -> {
                higher();
            });
            upperLabel.setOnMouseExited(event -> {
                lower();
            });
            upperLabel.setOnMouseClicked(event -> {
                Click(i, j);
            });
        }

        private void Click(int i, int j) {
            Location location = new Location(i, j);
            if (!players[turn].isHuman()){
                freeClick();
                return;
            }

            switch (stateOfMouseClickeds[turn]){
                case free:{
                    inFreeStateClickACell(location);
                    break;
                }
                case insiderMinionCardClicked:{
                    inStateOf_insiderMinionClicked_clicked(location);
                }
                case insertingCardClicked:{
                    inStateOf_insertingCard_clicked(location);
                }
                case specialPowerClicked:{
                    inStateOf_specialPower_clicked(location);
                }

            }

        }

        private void inStateOf_specialPower_clicked(Location location) {
            if (battle.canUseSpecialPower(players[turn].getHero(),location,false)){
                useSpecialPower(players[turn].getHero(),location);
            }else {
                freeClick();
            }

        }

        private void inStateOf_insertingCard_clicked(Location location) {
            if (battle.canInsert(playerSelectedCard[turn],location,false)){
                insert(playerSelectedCard[turn],location);
            }else {
                freeClick();
            }
        }

        private void inStateOf_insiderMinionClicked_clicked(Location location) {
            Minion selectedMinion = board.getMinionByLocation(location);
            if (selectedMinion==null){
                if (battle.canMove((Minion)playerSelectedCard[turn],location,false)){
                    move((Minion)playerSelectedCard[turn],location);
                }else {
                    freeClick();
                }
            }else {
                if (battle.canAttack((Minion) playerSelectedCard[turn],selectedMinion,false)){
                    attack((Minion) playerSelectedCard[turn],selectedMinion);
                }else {
                    freeClick();
                }
            }
        }

        private void inFreeStateClickACell(Location location){
            Minion selectedMinion = board.getMinionByLocation(location);
            if (selectedMinion==null){
                freeClick();
            }else {
                if (selectedMinion.getPlyNum()==turn){
                    playerSelectedCard[turn] = selectedMinion;
                    stateOfMouseClickeds[turn] = StateOfMouseClicked.insiderMinionCardClicked;
                    graphicalBoard.show_available_works(selectedMinion);
                }else {
                    freeClick();
                }
            }
        }

        private void lower() {
            downerLabel.getStyleClass().remove("downerLabel_higher");
            downerLabel.getStyleClass().add("downerLabel_lower");
        }

        private void higher() {
            downerLabel.getStyleClass().remove("downerLabel_lower");
            downerLabel.getStyleClass().add("downerLabel_higher");
        }

        public void addImageView(ImageView imageView) {
            imageView.setFitWidth(imageViewWidth);
            imageView.setFitHeight(imageViewHeight);
            imageView.relocate(-imageViewWidth / 5, -imageViewHeight / 3);
            pane.getChildren().add(imageView);
            pane.getChildren().remove(upperLabel);
            pane.getChildren().add(upperLabel);

        }

        public void canInsertCell() {
            uncolored();
            downerLabel.getStyleClass().add("downerLabel_green");
        }

        public void canMoveCell() {
            uncolored();
            downerLabel.getStyleClass().add("downerLabel_yellow");
        }

        public void canAttack() {
            uncolored();
            downerLabel.getStyleClass().add("downerLabel_red");
        }

        public void normalCell() {
            uncolored();
            downerLabel.getStyleClass().add("downerLabel_white");
        }

        public void uncolored(){
            downerLabel.getStyleClass().remove("downerLabel_yellow");
            downerLabel.getStyleClass().remove("downerLabel_red");
            downerLabel.getStyleClass().remove("downerLabel_blue");
            downerLabel.getStyleClass().remove("downerLabel_white");
            downerLabel.getStyleClass().remove("downerLabel_green");
        }
    }

    private void useSpecialPower(Hero hero, Location location) {

    }

    private class GraphicalHand {
        private int x = 500, y = 800;
        private Pane parentPane;
        private HBox hbox;
        private CardScene[] cardScenes;
        private int spacing = -20;
        private Hand hand;

        public GraphicalHand() {
            parentPane = new Pane();
            parentPane.relocate(x, y);

            hbox = new HBox();
            hbox.setSpacing(spacing);

            cardScenes = new CardScene[Hand.number_of_cards];
            for (int i = 0; i < Hand.number_of_cards; i++) {
                cardScenes[i] = new CardScene(i);
                hbox.getChildren().add(cardScenes[i].parentPane);
            }

            parentPane.getChildren().add(hbox);
        }

        public void setHand(Hand hand) {
            this.hand = hand;
        }

        public void updateHand() {
            ArrayList<Card> cards = hand.getCards();
            for (int i = 0; i < Hand.number_of_cards; i++) {
                cardScenes[i].setCardImageView(cards.get(i));
            }
        }

        public void insertCard(Card card){
            for (CardScene cardScene:cardScenes){
                if (cardScene.getCard()==card){
                    cardScene.removeCard();
                }
            }
        }
    }

    private class GraphicalBoard {
        private int boardWidth = 800;
        private int boardHeight = 450;
        private int cellGap = 5;

        public Pane parentPane;
        private GridPane gridPane;
        private CellPane[][] cellPanes;
        private Board board;

        public GraphicalBoard() {
            parentPane = new Pane();

            int width = Board.width;
            int height = Board.length;
            int cellWidth = (boardWidth - (cellGap * (width + 1))) / width;
            int cellHeight = (boardHeight - (cellGap * (height + 1))) / height;
            cellPanes = new CellPane[width][height];

            gridPane = new GridPane();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    cellPanes[i][j] = new CellPane(cellHeight, cellWidth, i, j);
                    gridPane.add(cellPanes[i][j].pane, i, j);
                }
            }
            gridPane.setHgap(cellGap);
            gridPane.setVgap(cellGap);
            gridPane.relocate(0, 0);

            parentPane.getChildren().add(gridPane);
            perspectiveGrid(parentPane);
        }

        private void perspectiveGrid(Pane perspectivePane) {
            PerspectiveTransform pt = new PerspectiveTransform();

            pt.setUlx(550);
            pt.setUly(240);
            pt.setUrx(1350);
            pt.setUry(240);
            pt.setLrx(1420);
            pt.setLry(680);
            pt.setLlx(480);
            pt.setLly(680);
            perspectivePane.relocate(550, 240);
            //perspectivePane.setEffect(pt);

        }

        public void show_available_works(Minion minion) {
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.length; j++) {
                    if (battle.canMove( minion, new Location(i, j), false)) {
                        cellPanes[i][j].canMoveCell();
                    }
                    if (battle.canAttack(minion, board.getCells()[i][j].getMinion(), false)) {
                        cellPanes[i][j].canAttack();
                    }
                }
            }
        }

        public void setCardInCell(Card card, Location location) {
            ImageView card_imageView = new ImageView(new File(card.getGraphicPack().getIdlePhotoAddress()).toURI().toString());
            cellPanes[location.getX()][location.getY()].addImageView(card_imageView);
        }

        private void show_available_cells_for_insert(Card card) {
            allCellsNormal();
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.length; j++) {
                    if (battle.canInsert(card, new Location(i, j), false)) {
                        cellPanes[i][j].canInsertCell();
                    }
                }
            }
        }

        private void allCellsNormal() {
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.length; j++) {
                    cellPanes[i][j].normalCell();
                }
            }
        }

        public void setBoard(Board board) {
            this.board = board;
        }
    }

    private enum StateOfMouseClicked {
        insertingCardClicked,
        insiderMinionCardClicked,
        specialPowerClicked,
        free;
    }
}
