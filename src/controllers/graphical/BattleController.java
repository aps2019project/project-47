package controllers.graphical;

import controllers.MyController;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import models.battle.Battle;
import models.battle.Hand;
import models.battle.Player;
import models.battle.board.Board;
import models.battle.board.Location;
import models.cards.Card;
import models.cards.GraphicPack;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class BattleController extends MyController implements Initializable {

    public AnchorPane anchorPane;
    private Board board;
    private Battle battle;
    private Card[] playerSelectedCard;
    private GraphicalHand graphicalHand;
    private GraphicalBoard graphicalBoard;
    private StateOfMouseClicked[] stateOfMouseClickeds;
    private int turn;
    private Player[] players;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBackground();
        playBackGroundMusic();
        creatBoardCells();
        creatHandScene();

    }

    private void setBackground() {
        Random random = new Random();
        String address = "src/resources/backgrounds/battleBackground/" + random.nextInt(11) + ".jpg";
        ImageView background = new ImageView(new File(address).toURI().toString());

        background.relocate(0, 0);
        background.setFitWidth(1920);
        background.setFitHeight(1080);
        background.setOnMouseClicked(event -> {
            freeClick();
        });
        anchorPane.getChildren().add(background);
    }

    private void playBackGroundMusic() {
        String address = "src/resources/music/battleBackground/1.m4a";
        Media media = new Media(new File(address).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public ImageView creatImageViewerOfMinion(Minion minion, MinionImageViewType minionImageViewType) {
        switch (minionImageViewType) {
            case death: {
                return new ImageView(new File(minion.getGraphicPack().getDeathPhotoAddress()).toURI().toString());
            }
            case idle: {
                return new ImageView(new File(minion.getGraphicPack().getIdlePhotoAddress()).toURI().toString());
            }
            case running: {
                return new ImageView(new File(minion.getGraphicPack().getMovePhotoAddress()).toURI().toString());
            }
            case attacking: {
                return new ImageView(new File(minion.getGraphicPack().getAttackPhotoAddress()).toURI().toString());
            }
            case breathing: {
                return new ImageView(new File(minion.getGraphicPack().getBreathingPhotoAddress()).toURI().toString());
            }
        }
        return null;
    }

    public void freeClick() {
//        System.out.println("free click");
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
        battle.getPlayers()[0].mana_rise(100);
        graphicalHand.setHand(battle.getPlayers()[0].getHand());
        graphicalBoard.setBoard(board);
        graphicalHand.updateHand();
        stateOfMouseClickeds = new StateOfMouseClicked[2];
        stateOfMouseClickeds[0] = StateOfMouseClicked.free;
        stateOfMouseClickeds[1] = StateOfMouseClicked.free;
        turn = battle.getTurn();
        players = battle.getPlayers();
        setHeroOnPlane_atStatingBattle();
    }

    public void setHeroOnPlane_atStatingBattle() {

        Hero hero0 = battle.getPlayers()[0].getHero();
        graphicalBoard.setMinionAtCell(hero0, new Location(0, Board.length / 2), MinionImageViewType.breathing);

        Hero hero1 = battle.getPlayers()[1].getHero();
        graphicalBoard.setMinionAtCell(hero1, new Location(Board.width - 1, Board.length / 2), MinionImageViewType.breathing);
    }

    private void insert(Card card, Location location) {
        graphicalHand.insertCard(card);
        graphicalBoard.insertCard(card, location);
        MyMediaPlayer.playEffectSoundOfACard(card, soundType.spawn);
        battle.insert(card, location);

        freeClick();
    }

    private void attack(Minion attacker, Minion defender) {
        int attackTime = 60;
        graphicalBoard.setMinionAtCell(attacker, attacker.getLocation(), MinionImageViewType.attacking);
        MyMediaPlayer.playEffectSoundOfACard(attacker, soundType.attack);
        MyMediaPlayer.playEffectSoundOfACard(defender, soundType.hit);
        AnimationTimer attackerAnimation = new AnimationTimer() {
            int lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime > attackTime) end();
                lastTime++;
            }

            public void end() {
                graphicalBoard.setMinionAtCell(attacker, attacker.getLocation(), MinionImageViewType.breathing);
                stop();
            }
        };
        attackerAnimation.start();
        if (battle.canCounterAttack(attacker, defender, false)) {
            AnimationTimer defenderAnimation = new AnimationTimer() {
                int lastTime = 0;
                boolean isChanged = false;

                @Override
                public void handle(long now) {
                    if (lastTime > 2 * attackTime) end();
                    if (lastTime > attackTime && (!isChanged)) {
                        isChanged = true;
                        graphicalBoard.setMinionAtCell(defender, defender.getLocation(), MinionImageViewType.attacking);
                        MyMediaPlayer.playEffectSoundOfACard(defender, soundType.attack);
                        MyMediaPlayer.playEffectSoundOfACard(attacker, soundType.hit);
                    }

                    lastTime++;
                }

                public void end() {
                    graphicalBoard.setMinionAtCell(defender, defender.getLocation(), MinionImageViewType.breathing);
                    battle.attack(attacker, defender);
                    stop();
                }
            };
            defenderAnimation.start();
        }
        battle.attack(attacker, defender);
        freeClick();

    }

    public void move(Minion minion, Location target) {

        Double moveTime = 1.0;

        ImageView runningImageView = creatImageViewerOfMinion(minion, MinionImageViewType.running);
        graphicalBoard.reSizeImageViewer(runningImageView);

        ImageView newImageViewAfterRunning = creatImageViewerOfMinion(minion, MinionImageViewType.breathing);
        graphicalBoard.reSizeImageViewer(newImageViewAfterRunning);
        graphicalBoard.relocateImageViewer(newImageViewAfterRunning);

        Location fromLocation = graphicalBoard.getLocateOfAnImage_inParentPane(minion.getLocation());
        Location finalLocation = graphicalBoard.getLocateOfAnImage_inParentPane(target);

        graphicalBoard.parentPane.getChildren().add(runningImageView);
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(moveTime));
        transition.setNode(runningImageView);

        transition.setFromX(fromLocation.getX());
        transition.setFromY(fromLocation.getY());
        transition.setToX(finalLocation.getX());
        transition.setToY(finalLocation.getY());

        transition.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                graphicalBoard.parentPane.getChildren().remove(runningImageView);
                graphicalBoard.setMinionAtCell(minion, target, MinionImageViewType.breathing);
            }
        });
        graphicalBoard.removeImageViewFromCell(minion.getLocation());
        transition.play();
        MyMediaPlayer.playEffectSoundOfACard(minion, soundType.run);
        battle.move(minion, target);

        freeClick();
    }

    private class CardScene {
        Pane parentPane;
        private ImageView ring;
        private ImageView cardImageView;
        private ImageView mana_view;
        private ImageView blackRing;
        private ImageView healthImageView;
        private ImageView attackPowerImageView;
        private Label lbl_health;
        private Label lbl_attackPower;
        Label lbl_manaNumbers;
        public int paneWidth = 200;
        public int paneHeight = 200;
        Circle downerCircle;
        Circle upperCircle;
        Card card;
        int numberOfCardScene;

        public CardScene(int numberOfCardScene) {

            this.numberOfCardScene = numberOfCardScene;

            parentPane = new Pane();
            parentPane.getStylesheets().add("layouts/stylesheets/battle/cardScene.css");
            parentPane.setPrefSize(paneWidth, paneHeight);

            Image blackRingImage = new Image(new File("src/resources/inBattle/cardSceneInserting/blackRing.png").toURI().toString());
            blackRing = new ImageView(blackRingImage);
            blackRing.setFitHeight(paneHeight);
            blackRing.setFitWidth(paneWidth);
            blackRing.relocate(0, 0);
            parentPane.getChildren().add(blackRing);

            downerCircle = new Circle();
            downerCircle.setRadius(Math.min(paneWidth, paneHeight) / 2.7);
            downerCircle.relocate(paneHeight / 7.5, paneWidth / 7.5);
            downerCircle.getStyleClass().add("downerCircle_firstStyle");
            downerCircle.getStyleClass().add("downerCircle_lower");
            parentPane.getChildren().add(downerCircle);

            Image ring_image = new Image(new File("src/resources/inBattle/cardSceneInserting/ring_glow_for_cardScene.png").toURI().toString());
            ring = new ImageView(ring_image);
            ring.setFitHeight(paneHeight);
            ring.setFitWidth(paneWidth);
            ring.relocate(0, 0);
            parentPane.getChildren().add(ring);

            Image mana_icon = new Image(new File("src/resources/inBattle/cardSceneInserting/icon_mana.png").toURI().toString());
            mana_view = new ImageView(mana_icon);
            mana_view.relocate(paneWidth / 2.8, paneHeight / 1.3);
            mana_view.setVisible(false);
            parentPane.getChildren().add(mana_view);

            lbl_manaNumbers = new Label();
            lbl_manaNumbers.getStyleClass().add("lbl_manaNumber");
            lbl_manaNumbers.relocate(paneWidth / 2.8 + 21, paneHeight / 1.3 + 10);
            parentPane.getChildren().add(lbl_manaNumbers);

            Image healthImage = new Image(new File("src/resources/inBattle/cardSceneInserting/healthImage.png").toURI().toString());
            healthImageView = new ImageView(healthImage);
            healthImageView.relocate(paneWidth / 6, paneHeight / 1.5);
            healthImageView.setFitWidth(paneWidth / 4);
            healthImageView.setFitHeight(paneHeight / 3);
            parentPane.getChildren().add(healthImageView);
            healthImageView.setVisible(false);

            lbl_health = new Label();
            lbl_health.relocate(paneWidth / 4.7, paneHeight / 1.3);
            lbl_health.getStyleClass().add("lbl_health");
            lbl_health.setTextFill(Color.GREEN);
            parentPane.getChildren().add(lbl_health);


            Image attackPowerImage = new Image(new File("src/resources/inBattle/cardSceneInserting/attackPowerImage.png").toURI().toString());
            attackPowerImageView = new ImageView(attackPowerImage);
            attackPowerImageView.relocate(paneWidth / 1.7, paneHeight / 1.5);
            attackPowerImageView.setFitWidth(paneWidth / 4);
            attackPowerImageView.setFitHeight(paneHeight / 3);
            parentPane.getChildren().add(attackPowerImageView);
            attackPowerImageView.setVisible(false);

            lbl_attackPower = new Label();
            lbl_attackPower.relocate(paneWidth / 1.6, paneHeight / 1.3);
            lbl_attackPower.getStyleClass().add("lbl_attackPower");
            lbl_attackPower.setTextFill(Color.RED);
            parentPane.getChildren().add(lbl_attackPower);

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
            mana_view.setVisible(true);
            set_mana_numbers(card.getMana());
            parentPane.getChildren().remove(upperCircle);
            parentPane.getChildren().add(cardImageView);
            parentPane.getChildren().add(upperCircle);
            if (!(card instanceof Spell)) {
                int attackPower = ((Minion) card).get_Real_AttackPower();
                int health = ((Minion) card).getHp();
                attackPowerImageView.setVisible(true);
                lbl_attackPower.setText(String.valueOf(attackPower));
                healthImageView.setVisible(true);
                lbl_health.setText(String.valueOf(health));
            }
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
            if (card == null) {
                freeClick();
            }
            if (!players[turn].isHuman()) {
                stateOfMouseClickeds[1 - turn] = null;
            }
            playerSelectedCard[turn] = card;
            stateOfMouseClickeds[turn] = StateOfMouseClicked.insertingCardClicked;
            graphicalBoard.show_available_cells_for_insert(card);
        }

        public void removeCard() {
            card = null;
            parentPane.getChildren().remove(cardImageView);
            cardImageView = null;
            lbl_manaNumbers.setText("");
            lbl_health.setText("");
            lbl_attackPower.setText("");
            attackPowerImageView.setVisible(false);
            healthImageView.setVisible(false);
            mana_view.setVisible(false);
        }

        public Card getCard() {
            return card;
        }
    }

    private class CellPane {
        public Pane parentPane;
        public Label downerLabel;
        Label upperLabel;
        ImageView imageView;
        Minion minion;

        public CellPane(int cellHeight, int cellWidth, int i, int j) {


            parentPane = new Pane();
            parentPane.getStylesheets().add("layouts/stylesheets/battle/cellPane.css");
            parentPane.setPrefSize(cellWidth, cellHeight);

            downerLabel = new Label();
            downerLabel.setPrefSize(cellWidth - 2, cellHeight - 2);
            downerLabel.getStyleClass().add("downerLabel_atFirst");
            downerLabel.getStyleClass().add("downerLabel_white");
            downerLabel.getStyleClass().add("downerLabel_lower");
            parentPane.getChildren().add(downerLabel);

            upperLabel = new Label();
            upperLabel.getStyleClass().add("upperLabel_atFirst");
            upperLabel.setPrefSize(cellWidth - 2, cellHeight - 2);
            parentPane.getChildren().add(upperLabel);

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
            if (!players[turn].isHuman()) {
                freeClick();
                return;
            }

            switch (stateOfMouseClickeds[turn]) {
                case free: {
                    inFreeStateClickACell(location);
                    break;
                }
                case insiderMinionCardClicked: {
                    inStateOf_insiderMinionClicked_clicked(location);
                }
                case insertingCardClicked: {
                    inStateOf_insertingCard_clicked(location);
                }
                case specialPowerClicked: {
                    inStateOf_specialPower_clicked(location);
                }

            }

        }

        private void inStateOf_specialPower_clicked(Location location) {
            if (battle.canUseSpecialPower(players[turn].getHero(), location, false)) {
                useSpecialPower(players[turn].getHero(), location);
            } else {
                freeClick();
            }

        }

        private void inStateOf_insertingCard_clicked(Location location) {
            if (battle.canInsert(playerSelectedCard[turn], location, false)) {
                insert(playerSelectedCard[turn], location);
            } else {
                freeClick();
            }
        }

        private void inStateOf_insiderMinionClicked_clicked(Location location) {
            Minion selectedMinion = board.getMinionByLocation(location);
            if (selectedMinion == null) {
                if (battle.canMove((Minion) playerSelectedCard[turn], location, false)) {
                    move((Minion) playerSelectedCard[turn], location);
                } else {
                    freeClick();
                }
            } else {
                if (battle.canAttack((Minion) playerSelectedCard[turn], selectedMinion, false)) {
                    attack((Minion) playerSelectedCard[turn], selectedMinion);
                } else {
                    freeClick();
                }
            }
        }

        private void inFreeStateClickACell(Location location) {
            Minion selectedMinion = board.getMinionByLocation(location);
            if (selectedMinion == null) {
                freeClick();
            } else {
                if (selectedMinion.getPlyNum() == turn) {
                    playerSelectedCard[turn] = selectedMinion;
                    stateOfMouseClickeds[turn] = StateOfMouseClicked.insiderMinionCardClicked;
                    graphicalBoard.show_available_works(selectedMinion);
                } else {
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

        public void addMinion(Minion minion) {
            ImageView minion_imageView = creatImageViewerOfMinion(minion, MinionImageViewType.idle);
            addImageView(minion_imageView);
            this.minion = minion;
        }

        private void addImageView(ImageView imageView) {
            graphicalBoard.reSizeImageViewer(imageView);
            graphicalBoard.relocateImageViewer(imageView);
            parentPane.getChildren().remove(this.imageView);
            parentPane.getChildren().add(imageView);
            parentPane.getChildren().remove(upperLabel);
            parentPane.getChildren().add(upperLabel);
            this.imageView = imageView;
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

        public void uncolored() {
            downerLabel.getStyleClass().remove("downerLabel_yellow");
            downerLabel.getStyleClass().remove("downerLabel_red");
            downerLabel.getStyleClass().remove("downerLabel_blue");
            downerLabel.getStyleClass().remove("downerLabel_white");
            downerLabel.getStyleClass().remove("downerLabel_green");
        }

        public void removeImageView() {
            parentPane.getChildren().remove(imageView);
            imageView = null;
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

        public void insertCard(Card card) {
            for (CardScene cardScene : cardScenes) {
                if (cardScene.getCard() == card) {
                    cardScene.removeCard();
                }
            }
        }
    }

    private class GraphicalBoard {
        private int boardWidth = 720;
        private int boardHeight = 410;
        private int cellGap = 5;
        int cellWidth;
        int cellHeight;

        public Pane parentPane;
        private GridPane gridPane;
        private CellPane[][] cellPanes;
        private Board board;

        public GraphicalBoard() {
            parentPane = new Pane();

            int width = Board.width;
            int height = Board.length;
            cellWidth = (boardWidth - (cellGap * (width + 1))) / width;
            cellHeight = (boardHeight - (cellGap * (height + 1))) / height;
            cellPanes = new CellPane[width][height];

            gridPane = new GridPane();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    cellPanes[i][j] = new CellPane(cellHeight, cellWidth, i, j);
                    gridPane.add(cellPanes[i][j].parentPane, i, j);
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
            perspectivePane.relocate(600, 350);
            //perspectivePane.setEffect(pt);

        }

        public void show_available_works(Minion minion) {
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.length; j++) {
                    if (battle.canMove(minion, new Location(i, j), false)) {
                        cellPanes[i][j].canMoveCell();
                    }
                    if (battle.canAttack(minion, board.getCells()[i][j].getMinion(), false)) {
                        cellPanes[i][j].canAttack();
                    }
                }
            }
        }

        public void insertCard(Card card, Location location) {
            if (card instanceof Spell) {
                insertSpell((Spell) card, location);
            } else {
                insertMinion((Minion) card, location);
            }
        }

        private void insertMinion(Minion minion, Location location) {
            setMinionAtCell(minion, location, MinionImageViewType.breathing);
            explosion(getLocateOfAnImage_inParentPane(location));
        }

        private void insertSpell(Spell spell, Location location) {
            spawn(getLocateOfAnImage_inParentPane(location));
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

        public void reSizeImageViewer(ImageView minionImageView) {
            Double imageViewWidth = graphicalBoard.cellWidth * 1.8;
            Double imageViewHeight = graphicalBoard.cellHeight * 1.8;
            minionImageView.setFitWidth(imageViewWidth);
            minionImageView.setFitHeight(imageViewHeight);
        }

        public void relocateImageViewer(ImageView minionImageView) {
            Location location = locateOfImageInCellPane();
            minionImageView.relocate(location.getX(), location.getY());
        }

        private Location locateOfImageInCellPane() {
            return new Location((int) (-graphicalBoard.cellWidth / 3), (int) (-graphicalBoard.cellHeight / 1.7));
        }

        public Location getLocateOfAnImage_inParentPane(Location location) {
            int x = (int) cellPanes[location.getX()][location.getY()].parentPane.getLayoutX();
            int y = (int) cellPanes[location.getX()][location.getY()].parentPane.getLayoutY();
            return new Location(x + locateOfImageInCellPane().getX()
                    , y + locateOfImageInCellPane().getY());
        }

        public void removeImageViewFromCell(Location location) {
            cellPanes[location.getX()][location.getY()].removeImageView();
        }

        public void explosion(Location location) {
            ImageView imageView = new ImageView(new File("src/resources/sprites/explosion_sprite.png").toURI().toString());
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imageView.relocate(location.getX(), location.getY());

            parentPane.getChildren().add(imageView);

            imageView.setViewport(new Rectangle2D(0, 0, 960, 1152));

            final Animation animation = new SpriteAnimation(
                    imageView,
                    Duration.millis(1000),
                    30, 6,
                    0, 0,
                    192, 192
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.setCycleCount(1);
            animation.setOnFinished(event -> {
                parentPane.getChildren().remove(imageView);
            });
            animation.play();
        }

        public void spawn(Location location) {
            ImageView imageView = new ImageView(new File("src/resources/sprites/spawn_sprite.png").toURI().toString());
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imageView.relocate(location.getX(), location.getY());

            parentPane.getChildren().add(imageView);

            imageView.setViewport(new Rectangle2D(0, 0, 2048, 1024));

            final Animation animation = new SpriteAnimation(
                    imageView,
                    Duration.millis(1000),
                    32, 8,
                    0, 0,
                    258, 256
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.setCycleCount(1);
            animation.setOnFinished(event -> {
                parentPane.getChildren().remove(imageView);
            });
            animation.play();
        }

        public void lighting(Location location) {
            ImageView imageView = new ImageView(new File("src/resources/sprites/lighting_sprite.png").toURI().toString());
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            imageView.relocate(location.getX(), location.getY());

            parentPane.getChildren().add(imageView);

            imageView.setViewport(new Rectangle2D(0, 0, 1024, 512));

            final Animation animation = new SpriteAnimation(
                    imageView,
                    Duration.millis(200),
                    8, 8,
                    0, 0,
                    128, 512
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.setCycleCount(3);
            animation.setOnFinished(event -> {
                parentPane.getChildren().remove(imageView);
            });
            animation.play();
        }

        public void setMinionAtCell(Minion minion, Location location, MinionImageViewType type) {
            String address = null;

            GraphicPack gp = minion.getGraphicPack();

            switch (type) {
                case attacking: {
                    address = gp.getAttackPhotoAddress();
                    break;
                }
                case death: {
                    address = gp.getDeathPhotoAddress();
                    break;
                }
                case breathing: {
                    address = gp.getBreathingPhotoAddress();
                    break;
                }
                case running: {
                    address = gp.getMovePhotoAddress();
                    break;
                }
                case idle: {
                    address = gp.getIdlePhotoAddress();
                    break;
                }
            }

            File file = new File(address);
            ImageView imageView = new ImageView(file.toURI().toString());
            System.out.println(location.getX()+","+location.getY());
            CellPane cellPane = cellPanes[location.getX()][location.getY()];
            cellPane.addImageView(imageView);
        }
    }

    private enum StateOfMouseClicked {
        insertingCardClicked,
        insiderMinionCardClicked,
        specialPowerClicked,
        free;
    }

    private enum MinionImageViewType {
        idle, attacking, death, breathing, running;
    }

    private static class MyMediaPlayer {
        static Double effectTime = 2.0;

        public static void playEffectSoundOfACard(Card card, soundType type) {
            String address = null;
            GraphicPack gp = card.getGraphicPack();
            switch (type) {
                case death: {
                    address = gp.getDeathSoundAddress();
                    break;
                }
                case spawn: {
                    address = gp.getSpawnSoundAddress();
                    break;
                }
                case run: {
                    address = gp.getMoveSoundAddress();
                    break;
                }
                case attack: {
                    address = gp.getAttackSoundAddress();
                    break;
                }
                case hit: {
                    address = gp.getHitSoundAddress();
                    break;
                }
                case impact: {
                    address = gp.getImpactSoundAddress();
                    break;
                }
            }
            Media media = new Media(new File(address).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setStopTime(Duration.seconds(effectTime));
            mediaPlayer.play();
        }
    }

    private enum soundType {
        death, attack, hit, impact,
        spawn, run
    }

}

