package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import controllers.console.AccountMenu;
import controllers.console.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Account;
import models.Shop;
import models.cards.Card;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.item.Item;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class UniversalShopController implements Initializable {
    Account loginAccount = AccountMenu.getLoginAccount();
    ArrayList<Card> accountCards;
    ArrayList<Item> accountItems;

    ArrayList<String> minionIds = new ArrayList<>();
    ArrayList<String> heroIds = new ArrayList<>();
    ArrayList<String> spellIds = new ArrayList<>();
    ArrayList<String> itemIds = new ArrayList<>();
    HashMap<String, SplitPane> forSearchCards = new HashMap<>();
    HashMap<String, SplitPane> cards = new HashMap<>();
    Shop shop = Shop.getInstance();

    public UniversalShopController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeIds();
        container.setPrefHeight(allParent.getPrefHeight());
        container.setPrefWidth(0.75 * allParent.getPrefWidth());
        heroesAndMinions.setPrefHeight(container.getPrefHeight());
        heroesAndMinions.setPrefWidth(container.getPrefWidth());
        topScrollPane.setPrefHeight(heroesAndMinions.getPrefHeight() / 2);
        bottomScrollPane.setPrefHeight(heroesAndMinions.getPrefHeight() / 2);
        topContainer.setPrefHeight(topScrollPane.getPrefHeight());
        topContainer.setPrefWidth(topScrollPane.getPrefWidth());
        bottomContainer.setPrefHeight(bottomScrollPane.getPrefHeight());
        bottomContainer.setPrefWidth(bottomScrollPane.getPrefWidth());
    }

    @FXML
    private SplitPane allParent;

    @FXML
    private AnchorPane container;

    @FXML
    private SplitPane heroesAndMinions;

    @FXML
    private ScrollPane topScrollPane;

    @FXML
    private HBox topContainer;

    @FXML
    private ScrollPane bottomScrollPane;

    @FXML
    private HBox bottomContainer;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchShowButton;

    @FXML
    void setMyCollectionMenu(ActionEvent event) {
        gotoMyCollection();
    }

    @FXML
    void setUniversalCollectionMenu(ActionEvent event) throws IOException {
        initializeCards();
        addCardsToContainer(cards.values());
        copyCardsInSearchSource();
    }

    @FXML
    void showResult(ActionEvent event) {
        String searched = searchField.getText();
        cards.clear();
        topContainer.getChildren().remove(0, topContainer.getChildren().size());
        bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
        if (searched.equals("")) {
            return;
        }
        for (String string : forSearchCards.keySet()){
            if (string.toLowerCase().contains(searched.toLowerCase()))
                cards.put(string, forSearchCards.get(string));
        }
        addCardsToContainer(cards.values());
    }

    public void initializeIds() {
        for (Integer i = 1; i <= 40; i++) {
            if (i <= 10) {
                if (i == 10) {
                    minionIds.add("m2".concat(i.toString()));
                    heroIds.add("h3".concat(i.toString()));
                    itemIds.add("i4".concat(i.toString()));
                    spellIds.add("s4".concat(i.toString()));
                } else {
                    minionIds.add("m20".concat(i.toString()));
                    heroIds.add("h30".concat(i.toString()));
                    itemIds.add("i40".concat(i.toString()));
                    spellIds.add("s40".concat(i.toString()));
                }
                continue;
            }
            if (i <= 20) {
                minionIds.add("m2".concat(i.toString()));
                if (i != 20) //because number of items is 19 surprisingly!!!
                    itemIds.add("i4".concat(i.toString()));
                spellIds.add("s4".concat(i.toString()));
                continue;
            }
            minionIds.add("m2".concat(i.toString()));
        }
    }

    public void initializeCards() {
        for (String id : minionIds)
            addNewCard(id, true);
        for (String id : heroIds)
            addNewCard(id, true);
        for (String id : spellIds)
            addNewCard(id, true);
        for (String id : itemIds)
            addNewCard(id, true);
    }

    private void addNewCard(String id, boolean buyOrSell) {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setDividerPosition(0, 0.82);
        splitPane.setDividerPosition(1, 0.18);
        splitPane.setPrefHeight(topContainer.getPrefHeight() - 20);
        splitPane.setPrefWidth(splitPane.getPrefHeight() * 0.82);
        JFXButton button = new JFXButton();
        button.setId(id);
        button.setPrefWidth(splitPane.getPrefWidth());
        button.setPrefHeight(0.18 * splitPane.getPrefHeight());
        Object cardOrItem = shop.find_in_shop(Integer.parseInt(id.substring(1)));
        if (buyOrSell) {

            button.setOnAction(event -> {
                Constants resultCode = shop.command_buy(Integer.parseInt(id.substring(1)));
                if (resultCode == Constants.SUCCESSFUL_BUY){

                }
                else if (resultCode == Constants.NOT_ENOUGH_MONEY){

                }
                else if (resultCode == Constants.HAD_BOUGHT_BEFORE){

                }
                else if (resultCode == Constants.NO_ACCOUNT_LOGGED_IN){

                }
            });
            if (cardOrItem instanceof Card)
                button.setText("Buy " + ((Card)cardOrItem).getName());
            else if (cardOrItem instanceof Item)
                button.setText("*Buy " + ((Item)cardOrItem).getName());
        } else {
            button.setOnAction(event -> shop.command_sell(Integer.parseInt(id.substring(1))));
            if (cardOrItem instanceof Card)
                button.setText("Sell " + ((Card)cardOrItem).getName());
            else if (cardOrItem instanceof Item)
                button.setText("*Sell " + ((Item)cardOrItem).getName());
        }
        ImageView imageView = new ImageView();
        imageView.setFitWidth(splitPane.getPrefWidth());
        Image image = new Image("/resources/general_portrait_image_hex_rook.png");
        imageView.setImage(image);
        imageView.setFitHeight(0.82 * splitPane.getPrefHeight());
        splitPane.getItems().add(0, imageView);
        splitPane.getItems().add(1, button);
        if (cardOrItem instanceof Card){
            cards.put(((Card) cardOrItem).getName(), splitPane);
        }
        else if (cardOrItem instanceof Item){
            cards.put(((Item) cardOrItem).getName(), splitPane);
        }
    }

    public void addCardsToContainer(Collection<SplitPane> source) {
        for (SplitPane s : source) {
            String id = s.getItems().get(1).getId();
            if (id.contains("m") || id.contains("h")) {
                topContainer.getChildren().add(s);
            } else if (id.contains("i") || id.contains("s")) {
                bottomContainer.getChildren().add(s);
            }
        }
    }

    public void gotoMyCollection() {
        if (loginAccount == null)
            return;
        accountCards = loginAccount.getCards();
        accountItems = loginAccount.getItems();
        topContainer.getChildren().remove(0, topContainer.getChildren().size());
        bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
        cards.clear();
        for (Card card : accountCards) {
            String id = "";
            if (card instanceof Spell)
                id = "s";
            else if (card instanceof Hero)
                id = "h";
            else if (card instanceof Minion)
                id = "m";
            id = id.concat(Integer.toString(card.getCode()));
            addNewCard(id, false);
        }
        copyCardsInSearchSource();
    }

    public void copyCardsInSearchSource(){
        for (String string : cards.keySet()){
            forSearchCards.put(string, cards.get(string));
        }
    }
}
