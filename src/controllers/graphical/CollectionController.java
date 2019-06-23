package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import controllers.console.AccountMenu;
import controllers.console.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Account;
import models.cards.Card;
import models.deck.Deck;
import models.item.Item;
import runners.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionController implements Initializable {
    Account loginAccount = AccountMenu.getLoginAccount();
    ArrayList<Deck> decks = loginAccount.getDecks();
    Deck currentDeck;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    void back(ActionEvent event) throws IOException {
        Parent root  = FXMLLoader.load(getClass().getResource("/layouts/mainMenu.fxml"));
        Main.getStage().getScene().setRoot(root);
        //Main.getStage().getScene().setRoot(AccountMenu.getRoot());
    }

    @FXML
    void ShowMyDecks(ActionEvent event) {
        topContainer.getChildren().remove(0, topContainer.getChildren().size());
        for (Deck deck : decks) {
            SplitPane deckCard = createCard("Show Deck", deck.getName(), new Image(deck.getHero().getGraphicPack().getShopPhotoAddress()),
                    event1 -> {
                        bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
                        String name = ((Button) event1.getSource()).getId();
                        ArrayList<Card> cards = deck.getCards();
                        for (Card card : cards) {
                            SplitPane splitPane = createCard("Show info of ", card.getName(), new Image(card.getGraphicPack().getShopPhotoAddress()), event2 -> {
                                //todo write on click for this
                            });
                            bottomContainer.getChildren().add(splitPane);
                        }
                        Item item = deck.getItem();
                        SplitPane splitPane = null;
                        if (item != null)
                            splitPane = createCard("Show info of", item.getName(), new Image("/resources/cards/general_portrait_image_hex_rook.png"), event22 -> {
                                //todo write on click for this
                            });
                        if (splitPane != null)
                            bottomContainer.getChildren().add(splitPane);
                    });
            topContainer.getChildren().add(deckCard);
        }
    }

    @FXML
    void createNewDeck(ActionEvent event) {
        String nameOfDeck = nameField.getText();
        if (nameOfDeck == null || nameOfDeck.equals("")) {
            //todo show an error of empty field
            return;
        }
        for (Deck deck : loginAccount.getDecks()) {
            if (deck.getName().equals(nameOfDeck)) {
                //todo choose an other name for your deck error
                return;
            }
        }
        Deck deck = new Deck(nameOfDeck);
        for (Card card : loginAccount.getCards()) {
            SplitPane splitPane = createCard("select ", card.getName(), new Image(card.getGraphicPack().getShopPhotoAddress()), event12 -> {
                for (Card card1 : deck.getCards()) {
                    if (card1.getName().equals(card.getName())) {
                        //todo show you have chose this before error
                        return;
                    }
                }
                deck.addCard(card);
                SplitPane addedCard = new SplitPane();
                addedCard = createCard("delete ", card.getName(), new Image(card.getGraphicPack().getShopPhotoAddress()), event121 -> {
                    deck.remove(card.getName());
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
                    //todo show you have selected an item before error
                    return;
                }
                deck.setItem(item);
                SplitPane addedCard = createCard("delete ", item.getName(), new Image("/resources/cards/general_portrait_image_hex_rook.png"), event131 -> {
                    deck.setItem(null);
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
        button.setPrefHeight(60);
        button.setStyle("-fx-background-radius: 60px");
        leftBar.getChildren().add(button);
        button.setPrefHeight(60);
        button.setLayoutX(10);
        button.setLayoutY(350);
        button.setPrefWidth(leftBar.getPrefWidth() - 20);
        button.setOnAction(event1 -> {
            Constants constant = deck.check_deck_correct();
            if (constant == Constants.NO_HERO) {
                System.out.println("no hero");
                //todo show no hero error
            }
            if (constant == Constants.NOT_20_CARDS) {
                System.out.println("not 20 cards");
                //todo show not 20 cards error
            }
            if (constant == Constants.MULTIPLE_HEROS) {
                System.out.println("multiple heros");
                //todo show multiple heros error
            }
            if (constant == Constants.CORRECT_DECK) {
                //todo show successful deck creation message
                System.out.println("successful deck");
                loginAccount.addDeck(deck);
                bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
                topContainer.getChildren().remove(0, topContainer.getChildren().size());
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
}
