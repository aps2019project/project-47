package controllers.graphical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfoenix.controls.JFXButton;
import controllers.console.AccountMenu;
import controllers.console.Constants;
import controllers.console.MainMenu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import layouts.AlertHelper;
import models.Account;
import models.cards.Card;
import models.cards.buff.Buff;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.cards.spell.effect.Effect;
import models.deck.Deck;
import models.item.Item;
import network.Client;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CollectionController implements Initializable {
    Account loginAccount = AccountMenu.getLoginAccount();
    ArrayList<Deck> decks = loginAccount.getDecks();
    Deck currentDeck;

    public static GsonBuilder gsonBuilder;
    public static Gson gson;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        container.setPrefHeight(allParent.getPrefHeight());
        container.setPrefWidth(0.75 * allParent.getPrefWidth());
        topScrollPane.setPrefHeight(container.getPrefHeight() / 2);
        bottomScrollPane.setPrefHeight(container.getPrefHeight() / 2);
        topContainer.setPrefHeight(topScrollPane.getPrefHeight());
        bottomContainer.setPrefHeight(bottomScrollPane.getPrefHeight());
        topContainer.setPrefWidth(topScrollPane.getPrefWidth());
        bottomContainer.setPrefWidth(bottomScrollPane.getPrefWidth());
    }

    @FXML
    private SplitPane allParent;

    @FXML
    private AnchorPane leftBar;

    @FXML
    private Button myDecksButton;

    @FXML
    private Button createNewDeckButton;

    @FXML
    private SplitPane container;

    @FXML
    private ScrollPane topScrollPane;

    @FXML
    private HBox topContainer;

    @FXML
    private ScrollPane bottomScrollPane;

    @FXML
    private HBox bottomContainer;

    @FXML
    private TextField nameField;

    @FXML
    private Button backButton;

    @FXML
    private VBox infoBox;

    @FXML
    private Button importDeckButton;

    @FXML
    private Button exportDeckButton;

    @FXML
    void back(ActionEvent event) throws IOException {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }

    @FXML
    void ShowMyDecks(ActionEvent event) {
        topContainer.getChildren().remove(0, topContainer.getChildren().size());
        for (Deck deck : decks) {
            SplitPane deckCard = createCard("Show and select", deck.getName(), new Image(deck.getHero().getGraphicPack().getShopPhotoAddress()),
                    event1 -> {
                        removeInfos(infoBox);
                        loginAccount.setMainDeck(deck);
                        bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
                        String name = ((Button) event1.getSource()).getId();
                        ArrayList<Card> cards = deck.getCards();
                        for (Card card : cards) {
                            SplitPane splitPane = createCard("Show info of ", card.getName(), new Image(card.getGraphicPack().getShopPhotoAddress()), event2 -> {
                                removeInfos(infoBox);
                                addInfoOfCard(card, infoBox);
                            });
                            bottomContainer.getChildren().add(splitPane);
                        }
                        Item item = deck.getItem();
                        SplitPane splitPane = null;
                        if (item != null)
                            splitPane = createCard("Show info of", item.getName(), new Image("/resources/cards/general_portrait_image_hex_rook.png"), event22 -> {
                                removeInfos(infoBox);
                                addItemInfo(item, infoBox);
                            });
                        if (splitPane != null)
                            bottomContainer.getChildren().add(splitPane);
                    });
            topContainer.getChildren().add(deckCard);
        }
    }

    public static void addItemInfo(Item item, VBox infoBox) {
        addInfo(infoBox, "name", item.getName());
        addInfo(infoBox, "price", Integer.toString(item.getPrice()));
        String effect = "";
        for (Effect effect1 : item.getEffects()) {
            for (Buff buff : effect1.getBuffs()) {
                effect.concat(String.valueOf(buff.getType()).replaceAll("_", " ")).concat("  ");
            }
        }
        addInfo(infoBox, "effects", effect);
    }

    @FXML
    void createNewDeck(ActionEvent event) {
        String nameOfDeck = nameField.getText();
        if (nameOfDeck == null || nameOfDeck.equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR , Client.getStage().getOwner() , "Error" , "Fill all Fields!");
            return;
        }
        for (Deck deck : loginAccount.getDecks()) {
            if (deck.getName().equals(nameOfDeck)) {
                AlertHelper.showAlert(Alert.AlertType.ERROR , Client.getStage().getOwner() , "Error" , "Choose an other name for your deck error!");
                return;
            }
        }
        Deck deck = new Deck(nameOfDeck);
        final Integer[] numberOfHeroes = {0};
        final Integer[] numberOfMinions = {0};
        final Integer[] numberOfSpells = {0};
        String stateOfItem = "none";
        String Heroes = addInfo(infoBox, "Heroes", Integer.toString(0));
        String Minions = addInfo(infoBox, "Minions", Integer.toString(0));
        String Spells = addInfo(infoBox, "Spells", Integer.toString(0));
        String itemSelected = addInfo(infoBox, "Item", "none");
        topContainer.getChildren().remove(0, topContainer.getChildren().size());
        bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
        for (Card card : loginAccount.getCards()) {
            SplitPane splitPane = createCard("select ", card.getName(), new Image(card.getGraphicPack().getShopPhotoAddress()), event12 -> {
                for (Card card1 : deck.getCards()) {
                    if (card1.getName().equals(card.getName())) {
                        AlertHelper.showAlert(Alert.AlertType.ERROR , Client.getStage().getOwner() ,"Error!" , "You have chose this before!");
                        return;
                    }
                }
                deck.addCard(card);
                if (card instanceof Hero)
                    changeValueOfInfo(infoBox, Heroes, (++numberOfHeroes[0]).toString());

                else if (card instanceof Minion)
                    changeValueOfInfo(infoBox, Minions, (++numberOfMinions[0]).toString());

                else if (card instanceof Spell)
                    changeValueOfInfo(infoBox, Spells, (++numberOfSpells[0]).toString());

                SplitPane addedCard = new SplitPane();
                addedCard = createCard("delete ", card.getName(), new Image(card.getGraphicPack().getShopPhotoAddress()), event121 -> {
                    deck.remove(card.getName());
                    if (card instanceof Spell)
                        changeValueOfInfo(infoBox, Spells, (--numberOfSpells[0]).toString());

                    else if (card instanceof Hero)
                        changeValueOfInfo(infoBox, Heroes, (--numberOfHeroes[0]).toString());

                    else if (card instanceof Minion)
                        changeValueOfInfo(infoBox, Minions, (--numberOfMinions[0]).toString());

                    for (int i = 0; i < bottomContainer.getChildren().size(); i++) {
                        if (bottomContainer.getChildren().get(i).getId().equals(card.getName())) {
                            bottomContainer.getChildren().remove(i);
                            break;
                        }
                    }
                });
                addedCard.setId(card.getName());
                bottomContainer.getChildren().add(addedCard);
            });
            topContainer.getChildren().add(splitPane);
        }
        for (Item item : loginAccount.getItems()) {
            SplitPane splitPane = createCard("select ", item.getName(), new Image("/resources/cards/general_portrait_image_hex_rook.png"), event13 -> {
                if (deck.getItem() != null) {
                    AlertHelper.showAlert(Alert.AlertType.ERROR , Client.getStage().getOwner() , "Error!" , "You have selected an item before!");
                    return;
                }
                deck.setItem(item);
                changeValueOfInfo(infoBox, itemSelected, item.getName());
                SplitPane addedCard = createCard("delete ", item.getName(), new Image("/resources/cards/general_portrait_image_hex_rook.png"), event131 -> {
                    deck.setItem(null);
                    changeValueOfInfo(infoBox, itemSelected, "none");
                    for (int i = 0; i < bottomContainer.getChildren().size(); i++) {
                        if (bottomContainer.getChildren().get(i).getId().equals(item.getName())) {
                            bottomContainer.getChildren().remove(i);
                        }
                    }
                });
                addedCard.setId(item.getName());
                bottomContainer.getChildren().add(addedCard);
            });
            topContainer.getChildren().add(splitPane);
        }
        Button button = new Button("finish");
        button.setId("finishButton");
        button.setPrefHeight(60);
        button.setStyle("-fx-background-radius: 60px");
        leftBar.getChildren().add(button);
        button.setPrefHeight(60);
        button.setLayoutX(10);
        button.setLayoutY(450);
        button.setPrefWidth(leftBar.getPrefWidth() - 20);
        button.setOnAction(event1 -> {
            Constants constant = deck.check_deck_correct();
            if (constant == Constants.NO_HERO) {
                System.out.println("no hero");
                AlertHelper.showAlert(Alert.AlertType.ERROR , Client.getStage().getOwner() , "Error!" , "You don't have hero!");
            }
            if (constant == Constants.NOT_20_CARDS) {
                System.out.println("not 20 cards");
                AlertHelper.showAlert(Alert.AlertType.ERROR , Client.getStage().getOwner() , "Error!" , "You don't have 20 Cards!");
            }
            if (constant == Constants.MULTIPLE_HEROS) {
                System.out.println("multiple heros");
                AlertHelper.showAlert(Alert.AlertType.ERROR , Client.getStage().getOwner() , "Error!" , "You have multiple heros!");
            }
            if (constant == Constants.CORRECT_DECK) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION , Client.getStage().getOwner() , "Deck created!" , "successful deck creation!");
                System.out.println("successful deck");
                loginAccount.addDeck(deck);
                bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
                topContainer.getChildren().remove(0, topContainer.getChildren().size());
                leftBar.getChildren().removeIf(node -> node.getId().equals("finishButtons"));
            }
        });
    }

    public SplitPane createCard(String titleOfButton, String name, Image image, EventHandler<ActionEvent> handler) {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setDividerPositions(0, 0.80);
        splitPane.setDividerPositions(1, 0.20);
        splitPane.setPrefHeight(topContainer.getPrefHeight() - 20);
        splitPane.setPrefWidth(splitPane.getPrefHeight() * 0.80);
        JFXButton showDeckButton = new JFXButton(titleOfButton + " " + name);
        showDeckButton.setStyle("-fx-font-weight: 900");
        showDeckButton.setOnAction(handler);
        showDeckButton.setId(name);
        showDeckButton.setPrefWidth(splitPane.getPrefWidth());
        showDeckButton.setPrefHeight(0.20 * splitPane.getPrefHeight());

        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(splitPane.getPrefWidth());
        imageView.setFitHeight(0.80 * splitPane.getPrefHeight());
        splitPane.getItems().add(0, imageView);
        splitPane.getItems().add(1, showDeckButton);

        return splitPane;
    }

    public static void removeInfos(VBox infoBox) {
        infoBox.getChildren().remove(1, infoBox.getChildren().size());
    }

    public static String addInfo(VBox infoBox, String title, String value) {
        Label label = new Label(title + ": " + value);
        infoBox.getChildren().add(label);
        label.setId(title);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 17;" +
                        "-fx-background-radius: 60px; " +
                        "-fx-background-color: #ce4534;"
        );
        label.setPadding(new Insets(10, 10, 10, 10));

        return title;
    }

    public static void changeValueOfInfo(VBox infoBox, String id, String newValue) {
        for (Node node : infoBox.getChildren()) {
            if (node.getId() != null && node.getId().equals(id)) {
                ((Label) node).setText(id + ": " + newValue);
            }
        }
    }

    public static void addInfoOfCard(Card card, VBox infoBox) {
        addInfo(infoBox, "name", card.getName());
        addInfo(infoBox, "manas required", Integer.toString(card.getMana()));
        addInfo(infoBox, "price", Integer.toString(card.getPrice()));
        if (card instanceof Minion) {
            addInfo(infoBox, "Health point", Integer.toString(((Minion) card).getRealHp()));
            addInfo(infoBox, "Attack point", Integer.toString(((Minion) card).getAp()));
            StringBuilder range = new StringBuilder();
            range.append(((Minion) card).getMinionTargetsType());
            addInfo(infoBox, "type", range.toString());
        }
        if (card instanceof Spell) {
            StringBuilder buffs = new StringBuilder();
            for (Effect effect : ((Spell) card).getEffects()) {
                for (Buff buff : effect.getBuffs()) {
                    buffs.append(buff.getType()).append("  ");
                }
            }
            addInfo(infoBox, "effects", buffs.toString().replaceAll("_", " "));
        }
    }

    @FXML
    void showDecksForExport(ActionEvent event) {
        topContainer.getChildren().remove(0, topContainer.getChildren().size());
        bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
        ArrayList<Deck> decks = loginAccount.getDecks();
        for (Deck deck : decks) {
            SplitPane deckCard = createCard("Export ", deck.getName(), new Image(deck.getHero().getGraphicPack().getShopPhotoAddress()),
                    event1 -> {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String json = gson.toJson(deck);
                        OutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream("src/JSONs/ExportedDecks/" + deck.getName() + ".json/");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Formatter formatter = new Formatter(outputStream);
                        formatter.format(json);
                        System.out.println(json);
                        formatter.flush();
                        formatter.close();

                    });
            topContainer.getChildren().add(deckCard);
        }
    }

    @FXML
    void showImportableDecks(ActionEvent event) {
        topContainer.getChildren().remove(0, topContainer.getChildren().size());
        bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        ArrayList<Deck> decks = new ArrayList<>();
        Path path = Paths.get("src/JSONs/ImportableDecks");
        File importableDecksFile = new File(path.toString());
        for (File file : importableDecksFile.listFiles()) {
            if (file.getName().contains(".json")) {
                String json = "";
                try {
                    Scanner scanner = new Scanner(file);
                    json = json.concat(scanner.nextLine());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Deck deck = gson.fromJson(json, Deck.class);
                decks.add(deck);
            }
        }
        for (Deck deck : decks) {
            SplitPane deckCard = createCard("Import ",
                    deck.getName(),
                    new Image(
                            deck.getHero()
                                    .getGraphicPack().getShopPhotoAddress()),
                    event1 -> {
                        if (!loginAccount.hasDeck(deck)) {
                            loginAccount.addDeck(deck);
                        } else {
                            AlertHelper.showAlert(Alert.AlertType.ERROR , Client.getStage().getOwner() , "Error!" , "you have a deck with this name error!");
                        }
                    });
            topContainer.getChildren().add(deckCard);
        }

    }
}
