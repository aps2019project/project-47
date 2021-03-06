package controllers.graphical;

import com.gilecode.yagson.YaGson;
import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import models.battle.*;
import models.battle.board.Board;
import models.battle.board.Location;
import models.cards.Card;
import models.cards.GraphicPack;
import models.cards.hero.Hero;
import models.cards.minion.ForceType;
import models.cards.minion.Minion;
import models.cards.minion.SideType;
import models.cards.spell.Spell;
import models.cards.spell.TargetForm;
import network.Client;
import network.Requests.account.UpdateAccountRequest;
import network.Requests.battle.BattleActionRequest;
import network.Requests.battle.MatchResultRequest;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.*;

import static models.battle.BattleActionType.*;

public class BattleController implements KeyListener {

    public static BattleController instance;

    {
        instance = this;
    }

    Double hideAndRiseSpeed = 50.0;
    Double aiPlayerSpeed = 150.0;
    Double reviewSpeed = 150.0;
    Double timeOfEachTurn = 2000.0;
    Double myTime;


    public AnchorPane anchorPane;
    private Board board;
    private Battle battle;
    private Card[] playerSelectedCard;
    private GraphicalHand graphicalHand;
    private GraphicalBoard graphicalBoard;
    private StateOfMouseClicked[] stateOfMouseClickeds;
    public BattleHistory lastBattleHistory;
    private boolean onServer;
    private boolean onReview;
    private int turn;
    private Player[] players;
    private AnimationTimer aiTimer;
    private AnimationTimer reviewTimer;
    private AnimationTimer turnTimer;
    private ImageView background;
    private MediaPlayer music;
    private boolean nowInAlertt;
    private GraphicButton specialPower;
    private ManaViewer[] manaViewers;
    private GraveYard graveYard;
    private YaGson yaGson = new YaGson();
    private boolean storyMod;
    private ImageView crusser;

    public BattleController() {
        anchorPane = new AnchorPane();
        initialize();
    }

    public Parent getRoot() {
        Client.stpoMusic();
        return anchorPane;
    }

    private String cheatText = "";

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        int id = keyEvent.getID();
        //if ()
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    public void setOnStoryMod(){
        storyMod = true;
    }

    public void setTurntime(Double time){
        timeOfEachTurn = time;
    }


    public void initialize() {
        setBackground();
        setMusic();
        creatBoardCells();
        creatHandScene();
        createButtons();
        creatAiPlayer();
        createReviewTimer();
        creatManaViewers();
        creatGraveYard();
        creatTurnTimer();
    }

    public void initializeBattle(Battle battle, boolean onServer, boolean onReview) {
        storyMod = false;
        myTime = 0.0;
        this.timeOfEachTurn = battle.turnTime;
        this.battle = battle;
        this.onServer = onServer;
        this.onReview = onReview;
        this.board = battle.getBoard();
        graphicalBoard.setBoard(board);
        playerSelectedCard = new Card[2];
        players = battle.getPlayers();
        if (!onReview) {
            lastBattleHistory = new BattleHistory(battle);
        }
        players[0].mana_rise(2);
        players[1].mana_rise(2);
        turn = battle.getTurn();
        stateOfMouseClickeds = new StateOfMouseClicked[2];
        stateOfMouseClickeds[0] = StateOfMouseClicked.free;
        stateOfMouseClickeds[1] = StateOfMouseClicked.free;
        setHeroOnPlane_atStatingBattle();
        manaViewers[0].update();
        manaViewers[1].update();
        update_specialPower_btn();
        graphicalBoard.updateFlagAndItem();
        updatesOfANewTurn();
        new MyAlert("battle started...").start();
    }

    private void setBackground() {
        Random random = new Random();
        String address = "src/resources/backgrounds/battleBackground/" + random.nextInt(11) + ".jpg";
        if (background == null) {
            background = new ImageView(new File(address).toURI().toString());
            anchorPane.getChildren().add(background);
        } else {
            background.setImage(new Image(new File(address).toURI().toString()));
        }
        background.relocate(0, 0);
        background.setFitWidth(1920);
        background.setFitHeight(1080);
        background.setOnMouseClicked(event -> {
            freeClick(turn);
        });
    }

    private void setMusic() {

        Random random = new Random();
        String address = "src/resources/music/battle/" + random.nextInt(20) + ".m4a";
        Media media = new Media(new File(address).toURI().toString());
        if (music != null) {
            music.stop();
        }
        music = new MediaPlayer(media);
        music.setVolume(0.2);
        music.play();
    }

    private void createButtons() {

        GraphicButton endTurn = new GraphicButton("END TURN");
        endTurn.setSize(300, 100);
        endTurn.reLocate(1500, 400);
        endTurn.setColor(Color.web("#abb056"));
        endTurn.setImageAddress("src/resources/inBattle/buttons/golden.png");
        endTurn.creat();
        endTurn.setOnClick(event -> {
            if (!players[turn].isHuman()) {
                return;
            }
            endTurnRequest();
        });
        anchorPane.getChildren().add(endTurn.getParentPane());

        specialPower = new GraphicButton("USE SPECIAL POWER");
        specialPower.setSize(500, 100);
        specialPower.reLocate(1400, 320);
        specialPower.setColor(Color.RED);
        specialPower.setImageAddress("src/resources/inBattle/buttons/red.png");
        specialPower.creat();
        specialPower.setOnClick(event -> {
            btn_specialPowerClicked();
        });
        anchorPane.getChildren().add(specialPower.getParentPane());

        GraphicButton help = new GraphicButton("HELP");
        help.setSize(200, 100);
        help.reLocate(1550, 480);
        help.setColor(Color.web("#6df25e"));
        help.setImageAddress("src/resources/inBattle/buttons/green.png");
        help.creat();
        help.setOnClick(event -> {
            if (!players[turn].isHuman()) {
                return;
            }
            if (onServer){
                return;
            }
            ai_do_only_one_action();
        });
        anchorPane.getChildren().add(help.getParentPane());


        GraphicButton changeBackground = new GraphicButton("change background");
        changeBackground.setSize(300, 100);
        changeBackground.reLocate(1550, 850);
        changeBackground.setColor(Color.BLACK);
        changeBackground.setImageAddress("src/resources/inBattle/buttons/left.png");
        changeBackground.creat();
        MyAlert changeBGAlert = new MyAlert("changing background");
        changeBGAlert.setMiddleEventHandler(event -> {
            setBackground();
        });
        changeBackground.setOnClick(event -> {
            if (!players[turn].isHuman()) {
                return;
            }
            changeBGAlert.start();
        });
        anchorPane.getChildren().add(changeBackground.getParentPane());


        GraphicButton chagneMusic = new GraphicButton("change music");
        chagneMusic.setSize(300, 100);
        chagneMusic.reLocate(1550, 950);
        chagneMusic.setColor(Color.BLACK);
        chagneMusic.setImageAddress("src/resources/inBattle/buttons/right.png");
        chagneMusic.creat();
        MyAlert chagneMusicAlert = new MyAlert("changing music");
        chagneMusicAlert.setMiddleEventHandler(event -> {
            setMusic();
        });
        chagneMusic.setOnClick(event -> {
            if (!players[turn].isHuman()) {
                return;
            }
            chagneMusicAlert.start();
        });
        anchorPane.getChildren().add(chagneMusic.getParentPane());

        GraphicButton closeButton = new GraphicButton("   ");
        closeButton.setSize(100, 100);
        closeButton.reLocate(1820, 0);
        closeButton.setImageAddress("src/resources/inBattle/buttons/close.png");
        closeButton.setColor(Color.BLACK);
        closeButton.creat();
        closeButton.setOnClick(event -> {
            battleFinish();
        });
        anchorPane.getChildren().add(closeButton.getParentPane());


        GraphicButton info = new GraphicButton("INFO");
        info.setSize(200, 100);
        info.reLocate(1600, 750);
        info.setColor(Color.BLACK);
        info.setImageAddress("src/resources/inBattle/buttons/middle.png");
        info.creat();
        info.setOnClick(event -> {
            if (!players[turn].isHuman()) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Battle\n");
            sb.append(players[0].getUserName());
            sb.append(" vs ");
            sb.append(players[1].getUserName());
            sb.append("\n");
            switch (battle.getMatchType()) {
                case collectFlag: {
                    sb.append(board.numOfFlagsOfPlayer(0, false));
                    sb.append(" : ");
                    sb.append(board.numOfFlagsOfPlayer(1, false));
                    break;
                }
                case keepFlag: {
                    sb.append(players[0].getNum_of_turns_with_flags());
                    sb.append(" : ");
                    sb.append(players[1].getNum_of_turns_with_flags());
                    break;
                }
                case kill: {
                    sb.append(players[0].getHero().getRealHp());
                    sb.append(" : ");
                    sb.append(players[1].getHero().getRealHp());
                    break;
                }
            }
            sb.append("\n");
            sb.append("--------");
            new MyAlert(sb.toString()).start();
        });
        anchorPane.getChildren().add(info.getParentPane());


    }

    private void creatBoardCells() {
        graphicalBoard = new GraphicalBoard();
        anchorPane.getChildren().add(graphicalBoard.parentPane);
    }

    private void creatHandScene() {
        graphicalHand = new GraphicalHand();
        anchorPane.getChildren().add(graphicalHand.parentPane);
    }

    private void setHeroOnPlane_atStatingBattle() {

        Hero hero0 = battle.getPlayers()[0].getHero();
        graphicalBoard.setMinionAtCell(hero0, hero0.getLocation(), MinionImageViewType.breathing, true);

        Hero hero1 = battle.getPlayers()[1].getHero();
        graphicalBoard.setMinionAtCell(hero1, hero1.getLocation(), MinionImageViewType.breathing, true);
    }

    private void creatAiPlayer() {
        aiTimer = new AnimationTimer() {
            int lastTime = 1;

            @Override
            public void handle(long now) {
                if (lastTime % aiPlayerSpeed == 0) {
                    if (!ai_do_only_one_action()) end();
                }
                lastTime++;
            }

            private void end() {
                endTurn();
                stop();
            }
        };
    }

    private void creatManaViewers() {
        manaViewers = new ManaViewer[2];

        manaViewers[0] = new ManaViewer(false, 0);
        manaViewers[0].parentPane.relocate(30, 20);
        anchorPane.getChildren().add(manaViewers[0].parentPane);

        manaViewers[1] = new ManaViewer(true, 1);
        manaViewers[1].parentPane.relocate(150, 20);
        anchorPane.getChildren().add(manaViewers[1].parentPane);
    }

    private void createReviewTimer() {
        reviewTimer = new AnimationTimer() {
            int historyActionCoiunter = 0;
            int lastTime = 1;

            @Override
            public void handle(long now) {
                if (lastTime % reviewSpeed == 0) {
                    doNextOneAction();
                    System.out.println("next one don");
                }
                lastTime++;
            }

            private void doNextOneAction() {
                BattleAction lastOne = lastBattleHistory.get(historyActionCoiunter);
                if (lastOne != null) {
                    doOneAction(lastOne);
                }
                historyActionCoiunter++;
            }
        };
    }

    private void creatGraveYard() {
        graveYard = new GraveYard();
        anchorPane.getChildren().add(graveYard.parentPane);
    }

    private void creatTurnTimer(){
        crusser = new ImageView(new Image(new File("src/resources/inBattle/crusser.png").toURI().toString()));
        crusser.setFitWidth(100);
        crusser.setFitHeight(100);
        anchorPane.getChildren().add(crusser);
        turnTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (myTime.equals(timeOfEachTurn)){
                    if (players[turn].isHuman()){
                        endTurnRequest();
                    }
                    myTime = 0.0;
                }
                crusser.relocate(1920*(myTime/ timeOfEachTurn),-50);
                myTime++;
            }
        };
        turnTimer.start();
    }


    private ImageView creatImageViewerOfMinion(Minion minion, MinionImageViewType minionImageViewType) {
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

    private void freeClick(int turn) {
        stateOfMouseClickeds[turn] = StateOfMouseClicked.free;
        playerSelectedCard[turn] = null;
        graphicalBoard.allCellsNormal();
    }

    public void death(Minion minion, Location location) {

//        if (!onReview){
//            lastBattleHistory.add(new BattleAction(minion.getCardId(),null,null,BattleActionType.death));
//        }

        int deathtime = 10;

        graphicalBoard.setMinionAtCell(minion, location, MinionImageViewType.death, false);
        AnimationTimer deathTimer = new AnimationTimer() {
            int lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime > deathtime) end();
                lastTime++;
            }

            public void end() {
                graphicalBoard.removeImageViewFromCell(location, true);
                graphicalBoard.updateFlagAndItem();
                this.stop();
            }
        };
        deathTimer.start();

    }

    private void insert(Card card, Location location) {

        if (!onReview) {
            lastBattleHistory.add(new BattleAction(card.getCardId(), null, location, BattleActionType.insert));
        }

        graphicalHand.insertCard(card);
        graphicalBoard.insertCard(card, location);
        MyMediaPlayer.playEffectSoundOfACard(card, soundType.spawn);
        battle.insert(card, location);
        manaViewers[turn].update();
        graphicalBoard.updateAllCellsNumbers();
        graphicalBoard.updateFlagAndItem();
        freeClick(turn);
    }

    private void attack(Minion attacker, Minion defender) {
        int attackTime = 60;

        if (!onReview) {
            lastBattleHistory.add(new BattleAction(attacker.getCardId(), defender.getCardId(), null, attack));
        }

        graphicalBoard.setMinionAtCell(attacker, attacker.getLocation(), MinionImageViewType.attacking, false);

        boolean counterAttackAvalabale = battle.canCounterAttack(attacker, defender, false);

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
                graphicalBoard.setMinionAtCell(attacker, attacker.getLocation(), MinionImageViewType.breathing, false);
                if (!counterAttackAvalabale) {
                    battle.attack(attacker, defender);
                    graphicalBoard.updateNumbers(attacker.getLocation());
                    graphicalBoard.updateNumbers(defender.getLocation());
                }
                stop();
            }
        };
        attackerAnimation.start();
        if (counterAttackAvalabale) {
            AnimationTimer defenderAnimation = new AnimationTimer() {
                int lastTime = 0;
                boolean isChanged = false;

                @Override
                public void handle(long now) {
                    if (lastTime > 2 * attackTime) end();
                    if (lastTime > attackTime && (!isChanged)) {
                        isChanged = true;
                        graphicalBoard.setMinionAtCell(defender, defender.getLocation(), MinionImageViewType.attacking, false);
                        MyMediaPlayer.playEffectSoundOfACard(defender, soundType.attack);
                        MyMediaPlayer.playEffectSoundOfACard(attacker, soundType.hit);
                    }

                    lastTime++;
                }

                public void end() {
                    graphicalBoard.setMinionAtCell(defender, defender.getLocation(), MinionImageViewType.breathing, false);
                    battle.attack(attacker, defender);
                    graphicalBoard.updateNumbers(attacker.getLocation());
                    graphicalBoard.updateNumbers(defender.getLocation());
                    stop();
                }
            };
            defenderAnimation.start();
        }

        graphicalBoard.updateFlagAndItem();

        freeClick(turn);

    }

    private void move(Minion minion, Location target) {

        Double moveTime = 1.0;

        if (!onReview) {
            lastBattleHistory.add(new BattleAction(minion.getCardId(), null, target, move));
        }

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
                graphicalBoard.setMinionAtCell(minion, target, MinionImageViewType.breathing, false);
                graphicalBoard.updateFlagAndItem();
            }
        });
        graphicalBoard.removeImageViewFromCell(minion.getLocation(), false);
        transition.play();
        MyMediaPlayer.playEffectSoundOfACard(minion, soundType.run);
        battle.move(minion, target);


        freeClick(turn);
    }

    private void useSpecialPower(Hero hero, Location target) {

        if (!onReview) {
            lastBattleHistory.add(new BattleAction(hero.getCardId(), null, target, BattleActionType.useSpecialPower));
        }

        graphicalBoard.lighting(graphicalBoard.getLocateOfAnImage_inParentPane(target));
        battle.use_special_power(hero, target);
        manaViewers[turn].update();

        graphicalBoard.updateFlagAndItem();
        graphicalBoard.updateAllCellsNumbers();
        graphicalBoard.allCellsNormal();
    }

    private void endTurn() {

        myTime = 0.0;
        aiTimer.stop();
        reviewTimer.stop();

        if (!onReview) {
            lastBattleHistory.add(new BattleAction(null, null, null, endTurn));
        }

        battle.changeTurn();
        turn = 1 - turn;

        freeClick(0);
        freeClick(1);

        String string = "It's turn of " + players[turn].getUserName() + " .";
        string = string + " \njust a moment ...";
        MyAlert myAlert = new MyAlert(string);
        myAlert.setOnfinishEvent(event -> {
            updatesOfANewTurn();
        });
        myAlert.start();

    }



    public void moveRequest(Minion minion, Location target){
        if (!onServer){
            move(minion,target);
            return;
        }
        BattleAction battleAction =  new BattleAction(minion.getCardId(),null,target,move);
        doAndSendBattleAction(battleAction);
    }

    public void attackRequest(Minion attacker, Minion defender){
        if (!onServer){
            attack(attacker,defender);
            return;
        }
        BattleAction battleAction =  new BattleAction(attacker.getCardId(),defender.getCardId(),null,attack);
        doAndSendBattleAction(battleAction);
    }

    public void insertRequest(Card card, Location target){
        if (!onServer){
            insert(card,target);
            return;
        }
        BattleAction battleAction =  new BattleAction(card.getCardId(),null,target,insert);
        doAndSendBattleAction(battleAction);
    }

    public void useSpecialPowerRequest(Hero hero, Location target){
        if (!onServer){
            useSpecialPower(hero,target);
            return;
        }
        BattleAction battleAction =  new BattleAction(hero.getCardId(),null,target,useSpecialPower);
        doAndSendBattleAction(battleAction);
    }

    public void endTurnRequest(){
        if (!onServer){
            endTurn();
            return;
        }
        BattleAction battleAction = new BattleAction(null, null, null, endTurn);
        doAndSendBattleAction(battleAction);
    }

    private void doAndSendBattleAction(BattleAction battleAction) {
        doOneAction(battleAction);
        BattleActionRequest battleActionRequest = new BattleActionRequest(AccountMenu.getLoginAccount().getAuthToken(), getUserNameOfOpponent(), battleAction);
        Client.getWriter().println(yaGson.toJson(battleActionRequest));
        Client.getWriter().flush();
    }

    public String getUserNameOfOpponent(){
        if (battle.getPlayers()[0].isHuman()){
            return battle.getPlayers()[1].getUserName();
        }
        return battle.getPlayers()[0].getUserName();
    }

    public void btn_specialPowerClicked() {
        if (!players[turn].isHuman()) {
            return;
        }
        stateOfMouseClickeds[turn] = StateOfMouseClicked.specialPowerClicked;
        graphicalBoard.show_available_cells_for_specialPower(players[turn].getHero());
    }

    private void update_specialPower_btn() {
        specialPower.hide();
        if (players[0].isHuman()) {
            if (battle.specialPowerAvalable(players[turn].getHero(), false)) {
                specialPower.show();
            }
        }
    }

    public void updatesOfANewTurn() {
        update_specialPower_btn();

        if (onReview) {
            reviewTimer.start();
        } else {
            if (!players[turn].isHuman()) {
                if (!onServer) {
                    aiTimer.start();
                }
            }
        }

        graphicalHand.setHand(players[turn].getHand());
        graphicalHand.updateHand();
        graphicalHand.updateHand();
        manaViewers[turn].update();
    }

    private boolean ai_do_only_one_action() {
        TargetForm insiderTargetForm = new TargetForm(0, 0, 9, 5, SideType.insider, ForceType.both, null, true);
        TargetForm enemyTargetForm = new TargetForm(0, 0, 9, 5, SideType.enemy, ForceType.both, null, true);
        ArrayList<Minion> enemyForces = board.find_all_minions_in_target(turn, new Location(0, 0), enemyTargetForm);
        ArrayList<Minion> insiderForces = board.find_all_minions_in_target(turn, new Location(0, 0), insiderTargetForm);
        ArrayList<Location> allLocations = new ArrayList<>();
        for (int i = 0; i < Board.width; i++) {
            for (int j = 0; j < Board.height; j++) {
                allLocations.add(new Location(i, j));
            }
        }
        //checking move
        battle.randomSort(allLocations);
        for (Minion minion : insiderForces) {
            for (Location location : allLocations) {
                if (battle.canMove(minion, location, false)) {
                    move(minion, location);
                    return true;
                }
            }
        }
        //checking battle
        battle.randomSort(allLocations);
        for (Minion attacker : insiderForces) {
            for (Minion defender : enemyForces) {
                if (attacker.isDeath()) break;
                if (defender.isDeath()) break;
                if (battle.canAttack(attacker, defender, false)) {
                    attack(attacker, defender);
                    return true;
                }
            }
        }
        //checking special power
        battle.randomSort(allLocations);
        for (Location location : allLocations) {
            if (battle.canUseSpecialPower(players[turn].getHero(), location, false)) {
                useSpecialPower(players[turn].getHero(), location);
                return true;
            }

        }
        //checking inserting
        battle.randomSort(allLocations);
        ArrayList<Card> handCards = new ArrayList<>();
        handCards.addAll(players[turn].getHand().getCards());
        for (Location location : allLocations) {
            for (Card card : handCards) {
                if (battle.canInsert(card, location, false)) {
                    insert(card, location);
                    return true;
                }
            }
        }
        return false;
    }

    public void doOneAction(BattleAction action) {
        switch (action.getType()) {
            case attack: {
                attackRes(action.getCardId1(), action.getCardId2());
                break;
            }
            case move: {
                moveRes(action.getCardId1(), action.getLocation());
                break;
            }
            case endTurn: {
                endTurn();
                break;
            }
            case useSpecialPower: {
                useSpecialPowerRes(action.getCardId1(), action.getLocation());
                break;
            }
            case death: {
                Minion minion = board.getMinionById(action.getCardId1());
                death(minion, minion.getLocation());
                break;
            }
            case insert: {
                insertRes(action.getCardId1(), action.getLocation());
                break;
            }
            case finish: {
                battleFinish();
                break;
            }
        }
    }

    public void battleFinish() {
        aiTimer.stop();
        reviewTimer.stop();
        if (music != null)
            music.stop();
        if (!onReview) {
            lastBattleHistory.add(new BattleAction(null, null, null, BattleActionType.finish));
        }
        if (turnTimer!=null){
            turnTimer.stop();
        }

        if (onServer) {
            //send finish battle requesta//todo
        }
        myTime = 0.0;
        Double hidenTime = 200.0;

        Pane pane = new Pane();
        pane.setPrefSize(1920, 1080);
        pane.setStyle("-fx-background-color: black");
        pane.setOpacity(0);
        anchorPane.getChildren().add(pane);
        AnimationTimer hidenTimer = new AnimationTimer() {
            int lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime > hidenTime) end();
                Double opacity = pane.getOpacity();
                opacity = Math.min(1, opacity + (1 / hidenTime));
                pane.setOpacity(opacity);
                lastTime++;
            }

            public void end() {
                stop();
                pane.setOpacity(1);
                anchorPane.getChildren().removeAll(graphicalBoard.parentPane, graphicalHand.parentPane);
                graphicalHand = null;
                graphicalBoard = null;
            }
        };

        String string = "riidiiii!\n";
        if (battle.getMatchResult() != null) {
            string = string + players[battle.getMatchResult().getWinner()].getUserName();
            string = string + " win!";
            string = string + "\n--------";
            analyseMatchResult();
        }

        MyAlert myAlert = new MyAlert(string);
        myAlert.setSpeeds(6000.0, 5.0);
        myAlert.setOnfinishEvent(event -> {
            Client.getStage().getScene().setRoot(MainMenu.getRoot());
        });
        myAlert.setMiddleEventHandler(event -> {
            //rainShit();
        });
        myAlert.start();
        hidenTimer.start();

        if (!onReview) {
            sendMatchResult();
        }
    }

    private void sendMatchResult() {
        MatchResult result = battle.getMatchResult();
        if (result==null)return;
        result.setBattleHistory(lastBattleHistory);
        AccountMenu.getLoginAccount().getMatchHistorys().add(result);
        YaGson yaGson = new YaGson();
        try {
            MatchResultRequest matchResultRequest =
                    new MatchResultRequest(AccountMenu.getLoginAccount().getAuthToken(), result);
            Client.getWriter().println(yaGson.toJson(matchResultRequest));
            Client.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AccountMenu.updateAccount();
    }

    private void rainShit() {
        int speed = 2;
        ImageView imageView = new ImageView(new Image(new File("src/resources/inBattle/finish/shit.png").toURI().toString()));
        imageView.setFitWidth(1920);
        imageView.setFitHeight(1080);
        imageView.relocate(0, 0);
        imageView.setViewport(new Rectangle2D(0, 1080 - 108, 192, 108));
        anchorPane.getChildren().add(imageView);
        AnimationTimer timer = new AnimationTimer() {
            int y = 1080 - 108;

            @Override
            public void handle(long now) {
                imageView.setViewport(new Rectangle2D(0, y, 192, 108));
                y -= speed;

                if (y <= 0) {
                    end();
                }
            }

            public void end() {
                anchorPane.getChildren().remove(imageView);
                stop();
            }
        };
        timer.start();
    }

    private void analyseMatchResult() {
        if (storyMod){
            MatchResult result = battle.getMatchResult();
            int winner = result.getWinner();
            String myName = AccountMenu.getLoginAccount().getUserName();
            boolean win = false;
            if (winner==0 && result.getUser0().equals(myName)){
                win=true;
            }
            if (winner==1 && result.getUser1().equals(myName)){
                win = true;
            }
            if (win) {
                AccountMenu.getLoginAccount().addStoryLvl();
                UpdateAccountRequest updateAccountRequest =  new UpdateAccountRequest(AccountMenu.getLoginAccount());
                String yaJson1 = yaGson.toJson(updateAccountRequest);
                Client.getWriter().println(yaJson1);
                Client.getWriter().flush();
            }
        }
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
        private Label lbl_manaNumber;
        private Label lbl_name;
        private int paneWidth;
        private int paneHeight;
        private Circle downerCircle;
        private Circle upperCircle;
        private Card card;
        int numberOfCardScene;

        public CardScene(int numberOfCardScene,int width,int height) {
            paneWidth = width;
            paneHeight = height;

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

            lbl_manaNumber = new Label();
            lbl_manaNumber.getStyleClass().add("lbl_manaNumber");
            lbl_manaNumber.relocate(paneWidth / 2.8 + 21, paneHeight / 1.3 + 10);
            parentPane.getChildren().add(lbl_manaNumber);

            Image healthImage = new Image(new File("src/resources/inBattle/cardSceneInserting/healthImage.png").toURI().toString());
            healthImageView = new ImageView(healthImage);
            healthImageView.relocate(paneWidth / 6, paneHeight / 1.5);
            healthImageView.setFitWidth(paneWidth / 4);
            healthImageView.setFitHeight(paneHeight / 3);
            parentPane.getChildren().add(healthImageView);
            healthImageView.setVisible(false);

            lbl_health = new Label();
            lbl_health.relocate(paneWidth / 5, paneHeight / 1.3);
            lbl_health.getStyleClass().add("lbl_health");
            lbl_health.setTextFill(Color.GREEN);
            lbl_health.setAlignment(Pos.CENTER);
            lbl_health.setPrefSize(paneWidth / 5.3, paneHeight / 10);
            parentPane.getChildren().add(lbl_health);


            Image attackPowerImage = new Image(new File("src/resources/inBattle/cardSceneInserting/attackPowerImage.png").toURI().toString());
            attackPowerImageView = new ImageView(attackPowerImage);
            attackPowerImageView.relocate(paneWidth / 1.7, paneHeight / 1.5);
            attackPowerImageView.setFitWidth(paneWidth / 4);
            attackPowerImageView.setFitHeight(paneHeight / 3);
            parentPane.getChildren().add(attackPowerImageView);
            attackPowerImageView.setVisible(false);

            lbl_attackPower = new Label();
            lbl_attackPower.relocate(paneWidth / 1.65, paneHeight / 1.3);
            lbl_attackPower.getStyleClass().add("lbl_attackPower");
            lbl_attackPower.setTextFill(Color.RED);
            lbl_attackPower.setAlignment(Pos.CENTER);
            lbl_attackPower.setPrefSize(paneWidth / 5.3, paneHeight / 10);
            parentPane.getChildren().add(lbl_attackPower);

            lbl_name = new Label();
            lbl_name.relocate(paneWidth / 4.5, -paneHeight / 10);
            lbl_name.setAlignment(Pos.CENTER);
            lbl_name.setTextFill(Color.WHITE);
            parentPane.getChildren().add(lbl_name);

            creatRing();

            upperCircle = new Circle();
            upperCircle.setRadius(Math.min(paneWidth, paneHeight) / 3.5);
            upperCircle.relocate(paneHeight / 5, paneWidth / 5);
            upperCircle.getStyleClass().add("upperCircle_firstStyle");
            upperCircle.setOnMouseEntered(event -> {
                if (card != null)
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

        public void setNullClick(){
            upperCircle.setOnMouseClicked(event -> {

            });
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
                freeClick(turn);
            }
            if (!players[turn].isHuman()) {
                return;
            }
            playerSelectedCard[turn] = card;
            stateOfMouseClickeds[turn] = StateOfMouseClicked.insertingCardClicked;
            graphicalBoard.show_available_cells_for_insert(card);
        }

        public void addCard(Card card, boolean delay) {
            parentPane.getChildren().remove(cardImageView);
            this.card = card;
            ImageView cardImageView = new ImageView(new File(card.getGraphicPack().getIdlePhotoAddress()).toURI().toString());
            this.cardImageView = cardImageView;
            cardImageView.relocate(0, -paneHeight / 10);
            cardImageView.setFitWidth(paneWidth);
            cardImageView.setFitHeight(paneHeight);
            parentPane.getChildren().remove(upperCircle);
            cardImageView.setOpacity(0);
            parentPane.getChildren().add(cardImageView);
            parentPane.getChildren().add(upperCircle);

            Double risingTime;
            if (delay) {
                risingTime = hideAndRiseSpeed;
            } else {
                risingTime = 0.0;
            }

            AnimationTimer hidenAnimation = new AnimationTimer() {
                int lastTime = 0;

                @Override
                public void handle(long now) {
                    if (lastTime > risingTime) end();
                    Double opacity = cardImageView.getOpacity();
                    opacity = Math.min(1, opacity + (1 / risingTime));
                    cardImageView.setOpacity(opacity);
                    lastTime++;
                }

                public void end() {
                    cardImageView.setOpacity(1);
                    updateNumbers();
                    stop();
                }
            };
            hidenAnimation.start();
        }

        public void removeCard(boolean delay) {
            if (card == null) return;
            Double removeTime;
            if (delay) {
                removeTime = hideAndRiseSpeed;
            } else {
                removeTime = 0.0;
            }

            card = null;

            AnimationTimer hidenAnimation = new AnimationTimer() {
                int lastTime = 0;

                @Override
                public void handle(long now) {
                    if (lastTime > removeTime) end();
                    if (cardImageView != null) {
                        Double opacity = cardImageView.getOpacity();
                        opacity = Math.max(0, opacity - (1 / removeTime));
                        cardImageView.setOpacity(opacity);
                        lastTime++;
                    }
                }

                private void end() {
                    parentPane.getChildren().remove(cardImageView);
                    cardImageView = null;
                    updateNumbers();
                    stop();
                }
            };
            hidenAnimation.start();
        }

        public void updateNumbers() {
            if (card == null) {
                lbl_manaNumber.setText("");
                lbl_health.setText("");
                lbl_attackPower.setText("");
                lbl_name.setText("");
                attackPowerImageView.setVisible(false);
                healthImageView.setVisible(false);
                mana_view.setVisible(false);
            } else {
                if (!(card instanceof Spell)) {
                    int attackPower = ((Minion) card).get_Real_AttackPower();
                    int health = ((Minion) card).getHp();
                    attackPowerImageView.setVisible(true);
                    lbl_attackPower.setText(String.valueOf(attackPower));
                    healthImageView.setVisible(true);
                    lbl_health.setText(String.valueOf(health));
                }
                lbl_name.setText(card.getCardId());
                mana_view.setVisible(true);
                lbl_manaNumber.setText(String.valueOf(card.getMana()));
            }
        }

        public Card getCard() {
            return card;
        }

        public void creatRing() {
            ImageView imageView = new ImageView(new File("src/resources/sprites/MmdRing.png").toURI().toString());
            imageView.setFitHeight(paneHeight);
            imageView.setFitWidth(paneWidth);
            imageView.setOpacity(0.5);
            imageView.relocate(0, 0);

            parentPane.getChildren().add(imageView);

            imageView.setViewport(new Rectangle2D(0, 0, 150, 1200));

            final Animation animation = new SpriteAnimation(
                    imageView,
                    Duration.millis(2000),
                    8, 1,
                    0, 0,
                    150, 150
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.setCycleCount(1000000);
            animation.setOnFinished(event -> {
                parentPane.getChildren().remove(imageView);
            });
            animation.play();
        }
    }

    private class CellPane {
        public Pane parentPane;
        public Label downerLabel;
        Label upperLabel;
        ImageView imageView;
        private Label lbl_health;
        private Label lbl_attackPower;
        private Minion minion;
        private Label lbl_name;
        private ImageView flagImage;
        private ImageView itemImage;
        int iCell;
        int jCell;

        public CellPane(int cellHeight, int cellWidth, int i, int j) {
            this.iCell = i;
            this.jCell = j;

            parentPane = new Pane();
            parentPane.getStylesheets().add("layouts/stylesheets/battle/cellPane.css");
            parentPane.setPrefSize(cellWidth, cellHeight);

            downerLabel = new Label();
            downerLabel.setPrefSize(cellWidth - 2, cellHeight - 2);
            downerLabel.getStyleClass().add("downerLabel_atFirst");
            downerLabel.getStyleClass().add("downerLabel_white");
            downerLabel.getStyleClass().add("downerLabel_lower");
            parentPane.getChildren().add(downerLabel);

            lbl_health = new Label();
            lbl_health.getStyleClass().add("lbl_health");
            lbl_health.relocate(0, cellHeight / 1.5);
            lbl_health.setTextFill(Color.GREEN);
            parentPane.getChildren().add(lbl_health);

            lbl_attackPower = new Label();
            lbl_attackPower.getStyleClass().add("lbl_attackPower");
            lbl_attackPower.relocate(cellWidth / 1.25, cellHeight / 1.5);
            lbl_attackPower.setTextFill(Color.RED);
            parentPane.getChildren().add(lbl_attackPower);

            lbl_name = new Label();
            parentPane.getChildren().add(lbl_name);

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

        public void updateNumbers() {
            if (minion == null) {
                lbl_name.setText("");
                lbl_health.setText("");
                lbl_attackPower.setText("");
                return;
            }

            lbl_name.setText(minion.getCardId());
            lbl_health.setText(String.valueOf(minion.getRealHp()));
            lbl_attackPower.setText(String.valueOf(minion.get_Real_AttackPower()));
        }

        private void Click(int i, int j) {
            Location location = new Location(i, j);
            if (!players[turn].isHuman()) {
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
                useSpecialPowerRequest(players[turn].getHero(), location);
            } else {
                freeClick(turn);
            }

        }

        private void inStateOf_insertingCard_clicked(Location location) {
            if (battle.canInsert(playerSelectedCard[turn], location, false)) {
                insertRequest(playerSelectedCard[turn], location);
            } else {
                freeClick(turn);
            }
        }

        private void inStateOf_insiderMinionClicked_clicked(Location location) {
            Minion selectedMinion = board.getMinionByLocation(location);
            if (selectedMinion == null) {
                if (battle.canMove((Minion) playerSelectedCard[turn], location, false)) {
                    moveRequest((Minion) playerSelectedCard[turn], location);
                } else {
                    freeClick(turn);
                }
            } else {
                if (battle.canAttack((Minion) playerSelectedCard[turn], selectedMinion, false)) {
                    attackRequest((Minion) playerSelectedCard[turn], selectedMinion);
                } else {
                    freeClick(turn);
                }
            }
        }

        private void inFreeStateClickACell(Location location) {
            Minion selectedMinion = board.getMinionByLocation(location);
            if (selectedMinion == null) {
                freeClick(turn);
            } else {
                if (selectedMinion.getPlyNum() == turn) {
                    playerSelectedCard[turn] = selectedMinion;
                    stateOfMouseClickeds[turn] = StateOfMouseClicked.insiderMinionCardClicked;
                    graphicalBoard.show_available_works(selectedMinion);
                } else {
                    freeClick(turn);
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

        public void setMinion(Minion minion, MinionImageViewType type, boolean delay) {
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

            this.minion = minion;
            addImageView(imageView, delay);

        }

        private void addImageView(ImageView imageView, boolean delay) {
            final Double risingTime;
            if (delay) {
                risingTime = hideAndRiseSpeed;
            } else {
                risingTime = 0.0;
            }
            parentPane.getChildren().remove(this.imageView);
            this.imageView = imageView;
            graphicalBoard.reSizeImageViewer(imageView);
            graphicalBoard.relocateImageViewer(imageView);
            imageView.setOpacity(0);
            parentPane.getChildren().remove(upperLabel);
            parentPane.getChildren().add(imageView);
            parentPane.getChildren().add(upperLabel);
            AnimationTimer hidenAnimation = new AnimationTimer() {
                int lastTime = 0;

                @Override
                public void handle(long now) {
                    if (lastTime > risingTime) end();
                    Double opacity = imageView.getOpacity();
                    opacity = Math.min(1, opacity + (1 / risingTime));
                    imageView.setOpacity(opacity);
                    lastTime++;
                }

                public void end() {
                    imageView.setOpacity(1);
                    updateNumbers();
                    stop();
                }
            };
            hidenAnimation.start();
        }

        private void removeMinion(boolean delay) {
            removeImageView(delay);
            this.minion = null;
            lbl_name.setText("");
            lbl_attackPower.setText("");
            lbl_health.setText("");
        }

        public void canInsertCell() {
            uncolored();
            downerLabel.getStyleClass().add("downerLabel_green");
        }

        public void canMoveCell() {
            uncolored();
            downerLabel.getStyleClass().add("downerLabel_yellow");
        }

        public void canAttackCell() {
            uncolored();
            downerLabel.getStyleClass().add("downerLabel_red");
        }

        public void canSpecialPowerCell() {
            canMoveCell();
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

        public void removeImageView(boolean delay) {
            final Double removingTime;
            if (delay) {
                removingTime = hideAndRiseSpeed;
            } else {
                removingTime = 0.0;
            }

            imageView.setOpacity(1);

            AnimationTimer hidenAnimation = new AnimationTimer() {
                int lastTime = 0;

                @Override
                public void handle(long now) {
                    if (lastTime > removingTime) end();
                    if (imageView != null) {
                        Double opacity = imageView.getOpacity();
                        opacity = Math.max(0, opacity - (1 / removingTime));
                        imageView.setOpacity(opacity);
                    }

                    lastTime++;
                }

                public void end() {
                    imageView.setOpacity(0);
                    parentPane.getChildren().remove(imageView);
                    imageView = null;
                    stop();
                }
            };
            hidenAnimation.start();
        }

        public void updateFlagAndItem() {
            updateFlag();
            updateItem();
        }

        private void updateFlag() {
            if (board.getCells()[iCell][jCell].getFlags().size() > 0) {
                if (flagImage == null) {
                    flagImage = new ImageView(new Image(new File("src/resources/inBattle/flag.png").toURI().toString()));
                    flagImage.relocate(0, 0);
                    flagImage.setFitWidth(50);
                    flagImage.setFitHeight(50);
                    parentPane.getChildren().remove(upperLabel);
                    parentPane.getChildren().add(flagImage);
                    parentPane.getChildren().add(upperLabel);
                }
            } else {
                if (flagImage != null) {
                    parentPane.getChildren().remove(flagImage);
                    flagImage = null;
                }
            }

        }

        private void updateItem() {
            if (board.getCells()[iCell][jCell].getItem() != null) {
                if (itemImage == null) {
                    itemImage = new ImageView(new Image(new File("src/resources/inBattle/box.png").toURI().toString()));
                    itemImage.relocate(0, 0);
                    itemImage.setFitWidth(70);
                    itemImage.setFitHeight(70);
                    parentPane.getChildren().remove(upperLabel);
                    parentPane.getChildren().add(itemImage);
                    parentPane.getChildren().add(upperLabel);
                }
            } else {
                if (itemImage != null) {
                    parentPane.getChildren().remove(itemImage);
                    itemImage = null;
                }
            }
        }

    }

    private class GraphicalHand {
        int cardSceneWidth = 200;
        int cardSceneHeight = 200;
        private int x = 50, y = 800;
        int spaceBetween_nextCard_and_hand = 250;
        private Pane parentPane;
        private HBox hbox;
        private CardScene[] cardScenes;
        private int spacing = -20;
        private Hand hand;
        private GraphicNextCard graphicNextCard;


        public GraphicalHand() {
            parentPane = new Pane();
            parentPane.relocate(x, y);

            hbox = new HBox();
            hbox.setSpacing(spacing);

            graphicNextCard = new GraphicNextCard();
            hbox.getChildren().add(graphicNextCard.parentPane);

            Pane spacingPane1 = new Pane();
            spacingPane1.setPrefSize(spaceBetween_nextCard_and_hand, 0);
            hbox.getChildren().add(spacingPane1);

            cardScenes = new CardScene[Hand.number_of_cards];
            for (int i = 0; i < Hand.number_of_cards; i++) {
                cardScenes[i] = new CardScene(i,cardSceneWidth,cardSceneHeight);
                hbox.getChildren().add(cardScenes[i].parentPane);
            }

            parentPane.getChildren().add(hbox);
        }

        public void setHand(Hand hand) {
            this.hand = hand;
        }

        public void updateHand() {
            for (int i = 0; i < Hand.number_of_cards; i++) {
                cardScenes[i].removeCard(true);
            }
            graphicNextCard.removeCard(true);

            AnimationTimer timer = new AnimationTimer() {
                int lastTime = 0;

                @Override
                public void handle(long now) {
                    if (lastTime > 1.2 * hideAndRiseSpeed) end();
                    lastTime++;
                }

                public void end() {
                    addNewCards();
                    stop();
                }

                public void addNewCards() {
                    ArrayList<Card> cards = hand.getCards();
                    for (int i = 0; i < cards.size(); i++) {
                        cardScenes[i].addCard(cards.get(i), true);
                    }
                    Card nextOneCard = hand.getNextOne();
                    if (nextOneCard != null) {
                        graphicNextCard.addCard(hand.getNextOne(), true);
                    }
                }
            };
            timer.start();
        }

        public void insertCard(Card card) {
            for (CardScene cardScene : cardScenes) {
                if (cardScene.getCard() == card) {
                    cardScene.removeCard(true);
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
            this.board = new Board();
            int width = Board.width;
            int height = Board.height;
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
            allCellsNormal();
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.height; j++) {
                    if (battle.canMove(minion, new Location(i, j), false)) {
                        cellPanes[i][j].canMoveCell();
                    }
                    if (battle.canAttack(minion, board.getCells()[i][j].getMinion(), false)) {
                        cellPanes[i][j].canAttackCell();
                    }
                }
            }
        }

        public void show_available_cells_for_specialPower(Hero hero) {
            allCellsNormal();
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.height; j++) {
                    if (battle.canUseSpecialPower(hero, new Location(i, j), false)) {
                        cellPanes[i][j].canSpecialPowerCell();
                    }
                }
            }
        }

        public void insertCard(Card card, Location location) {
            if (card instanceof Spell) {
                insertSpell(location);
            } else {
                insertMinion((Minion) card, location);
            }
        }

        public void updateAllCellsNumbers() {
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.height; j++) {
                    updateNumbers(new Location(i, j));
                }

            }
        }

        private void insertMinion(Minion minion, Location location) {
            setMinionAtCell(minion, location, MinionImageViewType.breathing, true);
            explosion(getLocateOfAnImage_inParentPane(location));
        }

        private void insertSpell(Location location) {
            spawn(getLocateOfAnImage_inParentPane(location));
        }

        private void show_available_cells_for_insert(Card card) {
            allCellsNormal();
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.height; j++) {
                    if (battle.canInsert(card, new Location(i, j), false)) {
                        cellPanes[i][j].canInsertCell();
                    }
                }
            }
        }

        private void allCellsNormal() {
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.height; j++) {
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
            return new Location((-graphicalBoard.cellWidth / 3), (int) (-graphicalBoard.cellHeight / 1.7));
        }

        public Location getLocateOfAnImage_inParentPane(Location location) {
            int x = (int) cellPanes[location.getX()][location.getY()].parentPane.getLayoutX();
            int y = (int) cellPanes[location.getX()][location.getY()].parentPane.getLayoutY();
            return new Location(x + locateOfImageInCellPane().getX()
                    , y + locateOfImageInCellPane().getY());
        }

        public void removeImageViewFromCell(Location location, boolean delay) {
            CellPane cellPane = cellPanes[location.getX()][location.getY()];
            cellPane.removeMinion(delay);
        }

        public void updateNumbers(Location location) {
            if (location == null) return;
            cellPanes[location.getX()][location.getY()].updateNumbers();
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
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
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

        public void setMinionAtCell(Minion minion, Location location, MinionImageViewType type, boolean delay) {
            CellPane cellPane = cellPanes[location.getX()][location.getY()];
            cellPane.setMinion(minion, type, delay);
        }

        public void updateFlagAndItem() {
            for (int i = 0; i < Board.width; i++) {
                for (int j = 0; j < Board.height; j++) {
                    cellPanes[i][j].updateFlagAndItem();
                }
            }
        }
    }

    private class GraphicNextCard {
        private int width = 260;
        private int height = 260;
        public Pane parentPane;
        private ImageView downerImageView;
        private ImageView ring;
        private Card card;
        private ImageView cardImageView;

        public GraphicNextCard() {
            parentPane = new Pane();
            parentPane.relocate(0, 0);
            parentPane.setPrefSize(width, height);

            Image blackRingImage = new Image(new File("src/resources/inBattle/nextCard/background.png").toURI().toString());
            downerImageView = new ImageView(blackRingImage);
            downerImageView.setFitHeight(height);
            downerImageView.setFitWidth(width);
            downerImageView.relocate(0, 0);
            parentPane.getChildren().add(downerImageView);

            Label label = new Label("NEXT CARD");
            label.setStyle("-fx-text-fill: blue;" +
                    "-fx-font-size: 40;" +
                    "-fx-font-weight: bold");
            label.relocate(width / 10, -height / 5);
            parentPane.getChildren().add(label);

            creatRing();
        }

        public void creatRing() {
            ring = new ImageView(new File("src/resources/sprites/MmdRing.png").toURI().toString());
            ring.setFitHeight(width);
            ring.setFitWidth(height);
            ring.setOpacity(0.8);
            ring.relocate(0, 0);

            parentPane.getChildren().add(ring);

            ring.setViewport(new Rectangle2D(0, 0, 150, 1200));

            final Animation animation = new SpriteAnimation(
                    ring,
                    Duration.millis(1000),
                    8, 1,
                    0, 0,
                    150, 150
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.setCycleCount(1000000);
            animation.setOnFinished(event -> {
                parentPane.getChildren().remove(ring);
            });
            animation.play();
        }

        public void addCard(Card card, boolean delay) {
            Double risingTime;
            if (delay) {
                risingTime = hideAndRiseSpeed;
            } else {
                risingTime = 0.0;
            }

            parentPane.getChildren().remove(cardImageView);
            ImageView imageView = new ImageView(new Image(new File(card.getGraphicPack().getIdlePhotoAddress()).toURI().toString()));
            this.card = card;
            this.cardImageView = imageView;
            imageView.relocate(0, -height / 7);
            imageView.setFitHeight(height);
            imageView.setFitWidth(width);
            imageView.setOpacity(0);
            parentPane.getChildren().remove(ring);
            parentPane.getChildren().add(imageView);
            parentPane.getChildren().add(ring);

            AnimationTimer hidenAnimation = new AnimationTimer() {
                int lastTime = 0;

                @Override
                public void handle(long now) {
                    if (lastTime > risingTime) end();
                    Double opacity = imageView.getOpacity();
                    opacity = Math.min(1, opacity + (1 / risingTime));
                    imageView.setOpacity(opacity);
                    lastTime++;
                }

                public void end() {
                    imageView.setOpacity(1);
                    stop();
                }
            };
            hidenAnimation.start();
        }

        public void removeCard(boolean delay) {
            if (card == null) return;
            this.card = null;

            Double removeTime;
            if (delay) {
                removeTime = hideAndRiseSpeed;
            } else {
                removeTime = 0.0;
            }

            AnimationTimer hidenAnimation = new AnimationTimer() {
                int lastTime = 0;

                @Override
                public void handle(long now) {
                    if (lastTime > removeTime) end();
                    if (cardImageView != null) {
                        Double opacity = cardImageView.getOpacity();
                        opacity = Math.max(0, opacity - (1 / removeTime));
                        cardImageView.setOpacity(opacity);
                    }
                    lastTime++;
                }

                public void end() {
                    parentPane.getChildren().remove(cardImageView);
                    cardImageView = null;
                    stop();
                }
            };
            hidenAnimation.start();

        }
    }

    public class GraphicButton {
        private int x;
        private int y;
        private int width;
        private int height;
        private Pane parentPane;
        private ImageView buttonView;
        private Label label;
        private String imageAddress;
        private String buttonText;
        private Paint textColor;

        public GraphicButton(String buttonText) {
            this.buttonText = buttonText;
        }

        public void setSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public void reLocate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setImageAddress(String address) {
            this.imageAddress = address;
        }

        public void setOnClick(EventHandler<MouseEvent> event) {
            label.setOnMouseClicked(event);
        }

        public void setColor(Paint color) {
            this.textColor = color;
        }

        public void creat() {
            parentPane = new Pane();
            parentPane.relocate(x, y);
            parentPane.getStylesheets().add("layouts/stylesheets/battle/button.css");
            parentPane.setPrefSize(width, height);

            Image buttonImage = new Image(new File(imageAddress).toURI().toString());
            buttonView = new ImageView(buttonImage);
            buttonView.relocate(0, 0);
            buttonView.setFitWidth(width);
            buttonView.setFitHeight(height);
            parentPane.getChildren().add(buttonView);

            label = new Label(buttonText);
            label.relocate(width * 0.15, height * 0.15);
            label.setPrefSize(width * 0.7, height * 0.7);
            label.setStyle("-fx-font-weight: bold;" +
                    "-fx-alignment: center;" +
                    "-fx-font-size: " + Math.min(width / (0.8 * buttonText.length()), height / 3) + ";");
            label.setTextFill(textColor);
            label.setOnMouseEntered(event -> {
                label.setTextFill(Color.WHITE);
            });
            label.setOnMouseExited(event -> {
                label.setTextFill(textColor);
            });
            parentPane.getChildren().add(label);
        }

        public Pane getParentPane() {
            return parentPane;
        }

        public Label getLabel() {
            return label;
        }

        public void hide() {
            anchorPane.getChildren().remove(parentPane);
        }

        public void show() {
            hide();
            anchorPane.getChildren().add(parentPane);
        }
    }

    public class GraveYard {
        int column = 5;
        int row = 4;
        int x = -300;
        int y = 0;
        Double oppeningTime = 0.5;
        ImageView frame;
        ImageView arrow;
        Pane parentPane;
        VBox leftVBox;
        VBox rightVBox;
        int cardWidth;
        int cardHeight;
        int width = 300;
        int height = 1000;
        int vBoxWidth;
        int vBoxHeight;
        int space = 50;
        boolean open;


        public GraveYard() {
            cardWidth = vBoxWidth /column;
            cardHeight = vBoxHeight /row;
            vBoxWidth = (width - 3 * space) / 2;
            vBoxHeight = height - 2 * space;
            parentPane = new Pane();
            parentPane.relocate(x, y);
            parentPane.setStyle("-fx-background-color: #05001a");
            parentPane.setPrefSize(width, height);

            leftVBox = new VBox();
            leftVBox.setPrefSize(vBoxWidth, vBoxHeight);
            leftVBox.relocate(space, space);
            leftVBox.setStyle("-fx-background-color: red");
            parentPane.getChildren().add(leftVBox);

            rightVBox = new VBox();
            rightVBox.setPrefSize(vBoxWidth, vBoxHeight);
            rightVBox.relocate(space * 2 + vBoxWidth, space);
            rightVBox.setStyle("-fx-background-color: blue");
            parentPane.getChildren().add(rightVBox);

            frame = new ImageView(new Image(new File("src/resources/inBattle/graveYard/frame.png").toURI().toString()));
            frame.setFitHeight(height);
            frame.setFitWidth(width/3 );
            frame.relocate(width, 0);
            parentPane.getChildren().add(frame);

            arrow = new ImageView(new Image(new File("src/resources/inBattle/graveYard/leftArrow.png").toURI().toString()));
            arrow.setFitWidth(50);
            arrow.setFitHeight(50);
            arrow.relocate(width, height / 1.5);
            arrow.setOnMouseClicked(event -> {
                clickArrow();
            });
            parentPane.getChildren().add(arrow);
        }

        public void clickArrow() {
            movePane();
            changeArrow();

            if (!open) {
                update();
            } else {
                clean();
            }

            System.out.println(open);
            open = !open;


        }

        public void movePane() {
            int movement = -x;
            if (open) {
                movement = x;
            }
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(parentPane);
            transition.setByX(movement);
            transition.setDuration(Duration.seconds(oppeningTime));
            transition.setCycleCount(1);
            transition.play();

        }

        public void changeArrow() {
            if (open) {
                arrow.setImage(new Image(new File("src/resources/inBattle/graveYard/leftArrow.png").toURI().toString()));
            } else {
                arrow.setImage(new Image(new File("src/resources/inBattle/graveYard/rightArrow.png").toURI().toString()));

            }
        }

        public void clean() {
            leftVBox.getChildren().clear();
            rightVBox.getChildren().clear();
        }

        public void update() {

            ArrayList<Card> leftCards = players[0].getGraveYard().getCards();
            ArrayList<Card> rightCards = players[1].getGraveYard().getCards();

            for (Card card : leftCards) {
                addACard(card, true);
            }

            for (Card card : rightCards) {
                addACard(card, false);
            }
        }

        public void addACard(Card card, boolean left) {
            VBox vBox;
            if (left) {
                vBox = leftVBox;
            } else {
                vBox = rightVBox;
            }
            CardScene cardScene = new CardScene(0,vBoxHeight/20,vBoxHeight/20);
            cardScene.addCard(card,false);
            cardScene.setNullClick();
            vBox.getChildren().add(cardScene.parentPane);
        }
    }


    class ManaViewer {
        int plyNum;
        int count;
        int containerWidth = 1500;
        int conainerHeight = 100;
        int faceSize = 150;
        int manaSize = 60;
        public Pane parentPane;
        private ImageView face;
        Double addAndRemoveTime = 10.0;
        private LinkedList<ImageView> imageViews = new LinkedList<>();
        private HBox container;

        public ManaViewer(boolean rightToLeft, int plyNum) {
            this.plyNum = plyNum;
            count = 0;
            imageViews = new LinkedList<>();
            parentPane = new Pane();

            int r = new Random().nextInt(15);
            face = new ImageView(new Image(new File("src/resources/inBattle/mana/face/" + r + ".png").toURI().toString()));
            face.setFitHeight(faceSize);
            face.setFitWidth(faceSize);
            parentPane.getChildren().add(face);

            container = new HBox();
            container.setPrefSize(containerWidth, conainerHeight);
            parentPane.getChildren().add(container);

            if (rightToLeft) {
                face.relocate(containerWidth, 0);
                container.relocate(0, -conainerHeight / 5);
                container.setRotate(180);
            } else {
                face.relocate(0, 0);
                container.relocate(faceSize, faceSize / 1.5);
            }
        }

        public synchronized void update() {
            int realValue = players[plyNum].getMana();
            if (realValue > count) {
                addMana(realValue - count);
            } else if (realValue < count) {
                removeMana(count - realValue);
            }
        }

        private void addMana(int value) {

            count += value;

            AnimationTimer addingTimer = new AnimationTimer() {
                int lastTime = 0;

                @Override
                public void handle(long now) {
                    if (lastTime % addAndRemoveTime == 0) {
                        addNewImageViewer();
                    }
                    brighting();
                    lastTime++;
                    if (lastTime == (value * addAndRemoveTime - 1)) {
                        end();
                    }
                }

                public void brighting() {
                    Double opacity = imageViews.getLast().getOpacity();
                    opacity = Math.min(1, opacity + (1 / addAndRemoveTime));
                    imageViews.getLast().setOpacity(opacity);
                }

                public void addNewImageViewer() {

                    ImageView newOne = new ImageView(new Image(new File("src/resources/inBattle/mana/icon_mana.png").toURI().toString()));
                    newOne.setFitWidth(manaSize);
                    newOne.setFitHeight(manaSize);
                    newOne.setOpacity(0);
                    container.getChildren().add(newOne);
                    imageViews.addLast(newOne);
                }

                public void end() {
                    stop();
                }
            };
            addingTimer.start();
        }

        private void removeMana(int value) {

            count -= value;

            AnimationTimer removingTimer = new AnimationTimer() {
                int lastTime = 1;

                @Override
                public void handle(long now) {
                    hiding();
                    if (lastTime % addAndRemoveTime == 0) {
                        container.getChildren().remove(imageViews.getLast());
                        imageViews.removeLast();
                    }
                    if (lastTime == (value * addAndRemoveTime)) {
                        end();
                    }
                    lastTime++;
                }

                public void hiding() {
                    Double opacity = imageViews.getLast().getOpacity();
                    opacity = Math.max(0, opacity - (1 / addAndRemoveTime));
                    imageViews.getLast().setOpacity(opacity);
                }

                public void end() {
                    stop();
                }
            };
            removingTimer.start();
        }

    }

    private enum StateOfMouseClicked {
        insertingCardClicked,
        insiderMinionCardClicked,
        specialPowerClicked,
        free
    }

    private enum MinionImageViewType {
        idle, attacking, death, breathing, running
    }

    public class MyAlert {
        private Double moveTime;
        private Double typingSpeed;
        private EventHandler finishEventHandler;
        private EventHandler middleEventHandler;
        private ImageView left;
        private ImageView up;
        private ImageView right;
        private Label label;
        private String string;

        public MyAlert(String string) {

            finishEventHandler = new EventHandler() {
                @Override
                public void handle(Event event) {

                }
            };

            moveTime = new Double(1000.0);
            typingSpeed = new Double(5.0);

            this.string = string;
            left = new ImageView(new Image(new File("src/resources/Alert/leftImage.png").toURI().toString()));
            left.relocate(-1920, 0);
            left.setFitWidth(1920);
            left.setFitHeight(1080);

            right = new ImageView(new Image(new File("src/resources/Alert/rightImage.png").toURI().toString()));
            right.relocate(1920, 0);
            right.setFitWidth(1920);
            right.setFitHeight(1080);

            up = new ImageView(new Image(new File("src/resources/Alert/upImage.png").toURI().toString()));
            up.relocate(0, -1080);
            up.setFitWidth(1920);
            up.setFitHeight(1080);

            label = new Label();
            label.getStylesheets().add("layouts/stylesheets/battle/myAlert.css");
//                label.getStyleClass().add("lbl");
            label.relocate(0, 0);
            label.setPrefSize(1920, 1080);
            label.setStyle("-fx-alignment: center;" +
                    "-fx-font-size: 80;" +
                    "-fx-font-weight: bold;");

        }

        public void start() {
            if (nowInAlertt) {
                return;
            }
            comImages();
            nowInAlertt = true;
        }

        public void setSpeeds(Double moveSpeed, Double typingSpeed) {
            this.moveTime = moveSpeed;
            this.typingSpeed = typingSpeed;
        }

        public void returnImage() {

            TranslateTransition upTT = new TranslateTransition(Duration.millis(moveTime), up);
            upTT.setByY(-1083);
            upTT.setCycleCount(1);

            TranslateTransition leftTT = new TranslateTransition(Duration.millis(moveTime), left);
            leftTT.setByX(-1920);
            leftTT.setCycleCount(1);

            TranslateTransition rightTT = new TranslateTransition(Duration.millis(moveTime), right);
            rightTT.setByX(1920);
            rightTT.setCycleCount(1);

            upTT.setOnFinished(event -> {
                anchorPane.getChildren().removeAll(left, right, up);
                nowInAlertt = false;
            });

            leftTT.setOnFinished(finishEventHandler);

            upTT.play();
            leftTT.play();
            rightTT.play();


        }

        public void comImages() {

            anchorPane.getChildren().removeAll(left, right, up);
            anchorPane.getChildren().addAll(left, right, up);

            TranslateTransition upTT = new TranslateTransition(Duration.millis(moveTime), up);
            upTT.setByY(1083);
            upTT.setCycleCount(1);

            TranslateTransition leftTT = new TranslateTransition(Duration.millis(moveTime), left);
            leftTT.setByX(1920);
            leftTT.setCycleCount(1);

            TranslateTransition rightTT = new TranslateTransition(Duration.millis(moveTime), right);
            rightTT.setByX(-1920);
            rightTT.setCycleCount(1);

            upTT.setOnFinished(event -> {
                MediaPlayer impact = new MediaPlayer(new Media(new File("src/resources/Alert/impact.m4a").toURI().toString()));
                impact.play();
                showString();
            });

            leftTT.setOnFinished(middleEventHandler);

            upTT.play();
            leftTT.play();
            rightTT.play();

        }

        public void showString() {
            char[] chars = string.toCharArray();
            anchorPane.getChildren().remove(label);
            anchorPane.getChildren().add(label);
            MediaPlayer typingSound = new MediaPlayer(new Media(new File("src/resources/Alert/typingSound.mp3").toURI().toString()));
            AnimationTimer stringShower = new AnimationTimer() {
                int lastTime;

                @Override
                public void handle(long now) {
                    if (lastTime > typingSpeed * (chars.length - 1)) {
                        end();
                    } else {
                        if (lastTime % typingSpeed == 0) {
                            label.setText(label.getText() + chars[(int) (lastTime / typingSpeed)]);
                        }
                    }
                    lastTime++;
                }

                public void end() {
                    label.setText("");
                    anchorPane.getChildren().remove(label);
                    typingSound.stop();
                    stop();
                    returnImage();
                }
            };

            stringShower.start();
            typingSound.play();
        }

        public void setOnfinishEvent(EventHandler event) {
            finishEventHandler = event;
        }

        public void setMiddleEventHandler(EventHandler middleEventHandler) {
            this.middleEventHandler = middleEventHandler;
        }

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


    public void attackRes(String attackerId, String defenderId) {
        Minion attacker = board.getMinionById(attackerId);
        Minion defender = board.getMinionById(defenderId);
        attack(attacker, defender);
    }

    public void moveRes(String minionId, Location target) {
        Minion minion = board.getMinionById(minionId);
        move(minion, target);
    }

    public void insertRes(String cardId, Location target) {
        Card card = battle.getCardByIdFromHands(cardId);
        insert(card, target);
    }

    public void endTurnRes() {
        endTurn();
    }

    public void useSpecialPowerRes(String heroId, Location target) {
        Hero hero = (Hero) board.getMinionById(heroId);
        useSpecialPower(hero, target);
    }

    public void setHistory(BattleHistory battleHistory) {
        this.lastBattleHistory = battleHistory;
    }



}

