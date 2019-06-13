package controllers.graphical;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import controllers.console.AccountMenu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import models.Shop;
import models.cards.Card;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.item.Item;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UniversalShopController implements Initializable {
    Account loginAccount = AccountMenu.getLoginAccount();
    ArrayList<Card> accountCards;
    ArrayList<Item> accountItems;

    ArrayList<String> minionIds = new ArrayList<>();
    ArrayList<String> heroIds = new ArrayList<>();
    ArrayList<String> spellIds = new ArrayList<>();
    ArrayList<String> itemIds = new ArrayList<>();
    ArrayList<SplitPane> cards = new ArrayList<>();
    Shop shop = Shop.getInstance();

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
        initializeCards();
        addCardsToContainer(cards);
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
    void setMyCollectionMenu(ActionEvent event) {
        gotoMyCollection();
    }

    @FXML
    void setUniversalCollectionMenu(ActionEvent event) throws IOException {
        FXMLLoader.load(getClass().getResource("/layouts/UniversalShop.fxml"));
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
        if (buyOrSell) {
            JFXSnackbar snackbar = new JFXSnackbar(container);

            button.setOnAction(event -> {
                int resultCode = shop.command_buy(Integer.parseInt(id.substring(1)));
                if (resultCode == 1){

                }
                else if (resultCode == -1){

                }
                else if (resultCode == -2){

                }
                else if (resultCode == -3){

                }
            });
            button.setText("Buy");
        } else {
            button.setOnAction(event -> shop.command_sell(Integer.parseInt(id.substring(1))));
            button.setText("sell");
        }
        ImageView imageView = new ImageView();
        imageView.setFitWidth(splitPane.getPrefWidth());
        Image image = new Image("/resources/general_portrait_image_hex_rook.png");
        imageView.setImage(image);
        imageView.setFitHeight(0.82 * splitPane.getPrefHeight());
        splitPane.getItems().add(0, imageView);
        splitPane.getItems().add(1, button);
        cards.add(splitPane);
    }

    public void addCardsToContainer(ArrayList<SplitPane> source) {
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
        topContainer.getChildren().removeAll();
        bottomContainer.getChildren().removeAll();
        cards.clear();
        for (Card card : accountCards) {
            String id = "";
            if (card instanceof Spell)
                id = "s";
            else if (card instanceof Hero)
                id = "h";
            else if (card instanceof Minion)
                id = "m";
            id = id.concat(card.getCardId());
            addNewCard(id, false);
        }
    }
}
