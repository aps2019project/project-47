package controllers.graphical;

import controllers.MyController;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import models.battle.Battle;
import models.battle.Hand;
import models.battle.Player;
import models.battle.board.Board;
import models.battle.board.Location;
import models.cards.Card;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class BattleController extends MyController implements Initializable {
    private int boardWidth = 800;
    private int boardHeight = 450;
    private int cellGap = 5;


    public AnchorPane anchorPane;
    private GridPane cellGrid;
    public Pane cellsPane;
    private Parent root;
    private Board board;
    private Battle battle;
    private Label[][] cells;
    private CellPane[][] cellPanes;
    private HashMap<String, ImageView> imageViewOfCards;
    private CardScene[] cardScenes;
    private Card[] playerSelectedCard;


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
        imageViewOfCards = new HashMap<>();
        buildGrid();
        creatHandScene();
    }

    private ImageView creatGif() {
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

    private void buildGrid() {

        GridPane cellGrid = new GridPane();

        int width = Board.width;
        int height = Board.length;
        int cellWidth = (boardWidth - (cellGap * (width + 1))) / width;
        int cellHeight = (boardHeight - (cellGap * (height + 1))) / height;

        cellPanes = new CellPane[Board.width][Board.length];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cellPanes[i][j] = new CellPane(cellHeight, cellWidth, i, j);
                cellGrid.add(cellPanes[i][j].pane, i, j);
            }
        }

        cellGrid.setHgap(cellGap);
        cellGrid.setVgap(cellGap);
        cellGrid.relocate(0, 0);

        Pane parentOfCellPanes = new Pane();
        parentOfCellPanes.getChildren().add(cellGrid);

        perspectiveGrid(parentOfCellPanes);
        anchorPane.getChildren().add(parentOfCellPanes);

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

    private void cellPaneClick(int i, int j) {
        Location location = new Location(i, j);
        int turn = battle.getTurn();
        Card selectedCard = playerSelectedCard[turn];

        if (!battle.getPlayers()[turn].isHuman()) {
            return;
        }
        if (playerSelectedCard[turn] == null) {
            return;
        }
        if (!playerSelectedCard[turn].isSpawn()) {
            if (battle.canInsert(selectedCard, location, false)) {
                insert(selectedCard, location);
            }
        } else {
            Minion selectedMinion = (Minion) selectedCard;
            if (battle.canMove(selectedMinion, location, false)) {
                move(selectedMinion, location);
            }
        }


    }

    private void creatHandScene() {
        int x = 500;
        int y = 800;
        int width = 500;
        int height = 200;
        Double multipleOfResizing_cardScene = 0.1;


        Pane parent = new Pane();
        parent.relocate(x, y);
        parent.setPrefSize(width, height);

        HBox hBox = new HBox();
        hBox.setSpacing(30);
        cardScenes = new CardScene[Hand.number_of_cards];

        for (int i = 0; i < Hand.number_of_cards; i++) {
//            ImageView imageView=new ImageView(new File("src/resources/cards/Mmd_test/Avalanche_idle.gif").toURI().toString());
            cardScenes[i] = new CardScene();
//            cardScenes[i].setCardImageView(imageView);
            hBox.getChildren().add(cardScenes[i].pane);
        }

        parent.getChildren().add(hBox);


        anchorPane.getChildren().add(parent);

    }

    public void initializeBattle(Battle battle) {
        this.battle = battle;
        playerSelectedCard = new Card[2];
        battle.getPlayers()[0].mana_rise(10);
        setHeroOnPlane();
        setHandCards(battle.getPlayers()[0].getHand());
    }

    public void setHeroOnPlane() {
        Hero hero0 = battle.getPlayers()[0].getHero();
        ImageView hero0_imageView = new ImageView(new File(hero0.getGraphicPack().getIdlePhotoAddress()).toURI().toString());
        creatImageViewMouseActions(hero0, hero0_imageView);
        cellPanes[0][Board.length / 2].addImageView(hero0_imageView);

        Hero hero1 = battle.getPlayers()[1].getHero();
        ImageView hero1_imageView = new ImageView(new File(hero1.getGraphicPack().getIdlePhotoAddress()).toURI().toString());
        creatImageViewMouseActions(hero1, hero1_imageView);
        cellPanes[Board.width - 1][Board.length / 2].addImageView(hero1_imageView);
    }

    public void creatImageViewMouseActions(Card card, ImageView imageView) {
        imageViewOfCards.put(card.getCardId(), imageView);
        imageView.setOnMouseClicked(event -> {
            int turn = battle.getTurn();
            if (!battle.getPlayers()[turn].isHuman()) {
                return;
            }
            Player player = battle.getPlayers()[turn];
            allCellsNormal();
            if (!card.isSpawn()) {
                show_available_cells_for_insert(card);
                playerSelectedCard[turn] = card;
            } else {//notSpawn

                if (playerSelectedCard[turn] == null) {
                    if (card.getPlyNum() == turn) {
                        playerSelectedCard[turn] = card;
                        show_available_works(card);
                    }
                } else {
                    //playerSelected card is not spawn and now , selected card
                    if (!playerSelectedCard[turn].isSpawn()) {
                        if (playerSelectedCard[turn] instanceof Spell) {
                            Minion selectedMinion = (Minion) card;
                            Spell spell = (Spell) playerSelectedCard[turn];
                            if (battle.canInsert(spell, selectedMinion.getLocation(), false)) {
                                insert(spell, selectedMinion.getLocation());
                            }
                        }
                    } else {
                        Minion attacker = (Minion) playerSelectedCard[turn];
                        Minion defender = (Minion) card;
                        if (battle.canAttack(attacker, defender, false)) {
                            attack(attacker, defender);
                        }
                    }
                }
            }
        });
    }

    private void insert(Card card, Location location) {

    }

    private void attack(Minion attacker, Minion defender) {

    }

    public void move(Minion minion, Location location) {

    }

    private void show_available_cells_for_insert(Card card) {
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

    public void show_available_works(Card card) {
        for (int i = 0; i < Board.width; i++) {
            for (int j = 0; j < Board.length; j++) {
                if (battle.canMove((Minion) card, new Location(i, j), false)) {
                    cellPanes[i][j].canMoveCell();
                }
                if (battle.canAttack((Minion) card, board.getCells()[i][j].getMinion(), false)) {
                    cellPanes[i][j].canAttack();
                }
            }
        }
    }

    public void setHandCards(Hand hand) {
        ArrayList<Card> cards = hand.getCards();
        for (int i = 0; i < Hand.number_of_cards; i++) {
            cardScenes[i].setCardImageView(cards.get(i));
        }
    }

    private class CardScene {
        Pane pane;
        public ImageView[] rings;
        public ImageView cardImageView;
        public ImageView mana_view;
        Label lbl_manaNumbers;
        public int ring_width = 200;
        public int ring_height = 200;

        public CardScene() {


            pane = new Pane();
            pane.getStylesheets().add("src/layouts/stylesheets/battlePlane.css");

            pane.setPrefSize(ring_width, ring_height);
            pane.setOnMouseEntered(event -> {
                higher();
            });
            pane.setOnMouseExited(event -> {
                lower();
            });

            rings = new ImageView[2];
            Image ring = new Image(new File("src/resources/inBattle/cardSceneInserting/glow_ring.png").toURI().toString());

            rings[0] = new ImageView(ring);
            rings[0].setFitHeight(ring_height);
            rings[0].setFitWidth(ring_width);
            rings[0].relocate(0, 0);

            rings[1] = new ImageView(ring);
            rings[1].setFitHeight(ring_height);
            rings[1].setFitWidth(ring_width);
            rings[1].relocate(0, 0);
            rings[1].setRotate(180);

            Image mana_icon = new Image(new File("src/resources/inBattle/cardSceneInserting/icon_mana.png").toURI().toString());
            mana_view = new ImageView(mana_icon);
            mana_view.relocate(ring_width / 2.8, ring_height / 1.3);

            lbl_manaNumbers = new Label("");
            lbl_manaNumbers.getStyleClass().add("lbl_manaNumber");
            lbl_manaNumbers.relocate(ring_width / 2.8 + 21, ring_height / 1.3 + 10);

            pane.getChildren().addAll(rings);
            pane.getChildren().add(mana_view);
            pane.getChildren().add(lbl_manaNumbers);

        }

        public void set_mana_numbers(int manaNumbers) {
            lbl_manaNumbers.setText(String.valueOf(manaNumbers));
        }

        public void setCardImageView(Card card) {
            cardImageView = new ImageView(new File("/resources/cards/Mmd_test/Avalanche_idle.gif").toURI().toString());
            creatImageViewMouseActions(card, cardImageView);
            this.cardImageView = cardImageView;
            cardImageView.relocate(5, -20);
            cardImageView.setFitWidth(ring_width);
            cardImageView.setFitHeight(ring_height);
            set_mana_numbers(card.getMana());
            pane.getChildren().add(cardImageView);
        }

        public void ring_rotate(int degree) {
            rings[0].setRotate(degree);
            rings[1].setRotate(degree + 180);
        }

        private void higher() {
            ring_rotate(60);
            pane.getStyleClass().add("cardSceneHigher");

        }

        private void lower() {
            ring_rotate(0);

        }

        private void click() {

        }
    }

    private class CellPane {
        int imageViewWidth = 150;
        int imageViewHeight = 150;
        public Pane pane;
        public Label label;
        Label upperLabel;

        public CellPane(int cellHeight, int cellWidth, int i, int j) {
            pane = new Pane();
            pane.getStylesheets().add("layouts/stylesheets/battlePlane.css");

            pane.setPrefSize(cellWidth, cellHeight);
            pane.setStyle("-fx-alignment: center;");

            label = new Label();
            label.getStyleClass().add("lbl_normalCell");
            label.setPrefSize(cellWidth - 2, cellHeight - 2);
            pane.getChildren().add(label);

            upperLabel = new Label();
            upperLabel.getStyleClass().add("lbl_upperLabel");
            upperLabel.setPrefSize(cellWidth - 2, cellHeight - 2);
            pane.getChildren().add(upperLabel);

            upperLabel.setOnMouseEntered(event -> {
                label.getStyleClass().add("lbl_higherCell");
            });
            upperLabel.setOnMouseExited(event -> {
                label.getStyleClass().add("lbl_lowerCell");
            });
            upperLabel.setOnMouseClicked(event -> {
                cellPaneClick(i, j);
            });
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
            label.getStyleClass().add("lbl_greenCell");
        }

        public void canMoveCell() {

            label.getStyleClass().add("lbl_yellowCell");
        }

        public void canAttack() {
            label.getStyleClass().add("lbl_redCell");
        }

        public void normalCell() {
            label.getStyleClass().add("lbl_normalCell");
        }
    }

    class manaViewer {
        private boolean rightToLeft;
        private LinkedList<ImageView> imageViews = new LinkedList<>();
        private HBox container;
        private Image activeManaImage;
        private Image inactiveManaImage;
        private int numberOfActiveManas;
        public manaViewer(boolean rightToLeft, int panePositionX, int panePositionY, int width, int height, String activeManaAddress, String inActiveManaAddress) {
            this.rightToLeft = rightToLeft;
            container = new HBox();
            container.setPrefWidth(width);
            container.setPrefHeight(height);
            container.setLayoutX(panePositionX);
            container.setLayoutY(panePositionY);
            this.activeManaImage = new Image(activeManaAddress);
            this.inactiveManaImage = new Image(inActiveManaAddress);
            for (int i = 0; i < 10; i++) {
                if ((i == 0 && rightToLeft) || (i == 9 && !rightToLeft)) {
                    imageViews.add(new ImageView(activeManaImage));
                } else {
                    imageViews.add(new ImageView(inactiveManaImage));
                }
            }
            this.numberOfActiveManas = 1;
        }

        public void addMana() {
            ImageView imageView = new ImageView(activeManaImage);
            if (rightToLeft) {
                imageViews.addFirst(imageView);
                imageViews.removeLast();
            } else {
                imageViews.addLast(imageView);
                imageViews.removeFirst();
            }
            numberOfActiveManas++;
        }

        public void removeMana() {
            ImageView imageView = new ImageView(inactiveManaImage);
            if (rightToLeft) {
                imageViews.addLast(imageView);
                imageViews.removeFirst();
            } else {
                imageViews.addFirst(imageView);
                imageViews.removeLast();
            }
            numberOfActiveManas --;
        }

        public void update() {
            clearContainer();
            for (ImageView imageView : imageViews) {
                container.getChildren().add(imageView);
            }
        }

        //use matchManaAndUpdate after each action related to mana
        public void matchManasAndUpdate(Player player){
            if (player.getMana() > numberOfActiveManas){
                while (player.getMana() > numberOfActiveManas)
                    addMana();
            }
            if (player.getMana() < numberOfActiveManas){
                while (player.getMana() < numberOfActiveManas)
                    removeMana();
            }
            update();
        }

        public void clearContainer() {
            container.getChildren().remove(0, container.getChildren().size());
        }

    }
}


