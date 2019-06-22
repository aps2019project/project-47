package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import controllers.console.AccountMenu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Account;
import models.cards.Card;
import models.deck.Deck;
import models.item.Item;

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
    void ShowMyDecks(ActionEvent event) {
        topContainer.getChildren().remove(0, topContainer.getChildren().size());
        for (Deck deck : decks) {
            SplitPane deckCard = createCard("Show Deck", deck.getName(), new Image("/resources/cards/Zahak_logo.png"),
                    event1 -> {
                        bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
                        String name = ((Button) event1.getSource()).getId();
                        ArrayList<Card> cards = deck.getCards();
                        for (Card card : cards) {
                            SplitPane splitPane = createCard("Show info of ", card.getName(), new Image("/resources/cards/Zahak_logo.png"), event2 -> {
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

    }

    public SplitPane createCard(String titleOfButton, String name, Image image, EventHandler<ActionEvent> handler) {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setDividerPositions(0, 0.80);
        splitPane.setDividerPositions(1, 0.20);
        splitPane.setPrefHeight(topContainer.getPrefHeight() - 20);
        splitPane.setPrefWidth(splitPane.getPrefHeight() * 0.80);
        JFXButton showDeckButton = new JFXButton(titleOfButton + " " + name);
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
