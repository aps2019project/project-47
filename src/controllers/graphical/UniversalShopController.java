package controllers.graphical;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.jfoenix.controls.JFXButton;
import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import layouts.AlertHelper;
import models.Account;
import models.Shop;
import models.cards.Card;
import models.cards.hero.Hero;
import models.cards.minion.Minion;
import models.cards.spell.Spell;
import models.item.Item;
import network.Client;
import network.Requests.shop.BuyRequest;
import network.Requests.shop.SellRequest;
import network.Responses.Response;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class UniversalShopController implements Initializable {

    public static YaGsonBuilder yaGsonBuilder = new YaGsonBuilder();
    public static YaGson yaGson = yaGsonBuilder.create();

    public static UniversalShopController instance;

    {
        instance = this;
    }

    private Account loginAccount = AccountMenu.getLoginAccount();
    ArrayList<Card> accountCards;
    ArrayList<Item> accountItems;

    ArrayList<String> minionIds = new ArrayList<>();
    ArrayList<String> heroIds = new ArrayList<>();
    ArrayList<String> spellIds = new ArrayList<>();
    ArrayList<String> itemIds = new ArrayList<>();
    HashMap<String, SplitPane> forSearchCards = new HashMap<>();
    HashMap<String, SplitPane> cards = new HashMap<>();

    public void setLoginAccount(Account loginAccount) {
        this.loginAccount = loginAccount;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buyID("0000");
        initializeIds();
        container.setPrefHeight(allParent.getPrefHeight());
        container.setPrefWidth(0.75 * allParent.getPrefWidth());
        money.setText("Money : " + loginAccount.getMoney());
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
    HBox topContainer;

    @FXML
    private ScrollPane bottomScrollPane;

    @FXML
    HBox bottomContainer;

    @FXML
    private TextField searchField;

    @FXML
    private Label money;

    @FXML
    private VBox infoBox;

    @FXML
    void setMyCollectionMenu(ActionEvent event) {
        gotoMyCollection();
        addCardsToContainer(cards.values());
    }

    @FXML
    public void setUniversalCollectionMenu() {
        initializeCards();
        addCardsToContainer(cards.values());
        copyCardsInSearchSource();
        money.setText("Money : " + loginAccount.getMoney());
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
        for (String string : forSearchCards.keySet()) {
            if (string.toLowerCase().contains(searched.toLowerCase()))
                cards.put(string, forSearchCards.get(string));
        }
        addCardsToContainer(cards.values());
    }

    public Label getMoney() {
        return money;
    }

    public void initializeIds() {
        for (Integer i = 1; i <= 40; i++) {
            if (i <= 10) {
                if (i == 10) {
                    minionIds.add("m2".concat(i.toString()));
                    heroIds.add("h3".concat(i.toString()));
                    itemIds.add("i4".concat(i.toString()));
                    spellIds.add("s1".concat(i.toString()));
                } else {
                    minionIds.add("m20".concat(i.toString()));
                    heroIds.add("h30".concat(i.toString()));
                    itemIds.add("i40".concat(i.toString()));
                    spellIds.add("s10".concat(i.toString()));
                }
                continue;
            }
            if (i <= 20) {
                minionIds.add("m2".concat(i.toString()));
                if (i != 20) //because number of items is 19 surprisingly!!!
                    if (i <= 11)
                        itemIds.add("i4".concat(i.toString()));
                spellIds.add("s1".concat(i.toString()));
                continue;
            }
            minionIds.add("m2".concat(i.toString()));
        }
    }


    public void addId(Card card){
        if (card instanceof Spell)
            spellIds.add("s" + card.getCardId());
        if (card instanceof Hero)
            heroIds.add("h" + card.getCardId());
        if (card instanceof Minion)
            minionIds.add("m" + card.getCardId());
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

    public void showBuyResponse(Response response) {
        try {
            switch (response.getRequestResult()) {
                case NULL_REQUEST:
                case SUCCESSFUL_BUY:
                    setUniversalCollectionMenu();
                    break;
                case NOT_ENOUGH_MONEY:
                    AlertHelper.showAlert(Alert.AlertType.ERROR, Client.getStage().getOwner(), "Error!", "You don't have enough money!");
                    break;
                case HAD_BOUGHT_BEFORE:
                    AlertHelper.showAlert(Alert.AlertType.ERROR, Client.getStage().getOwner(), "Error!", "You have bought this thing!");
                    break;
                case NO_ACCOUNT_LOGGED_IN:
                    AlertHelper.showAlert(Alert.AlertType.ERROR, Client.getStage().getOwner(), "!WTF!", "No account logged in! WTF!!!!");
                    break;
                case NOT_EXISTS:
                    AlertHelper.showAlert(Alert.AlertType.ERROR, Client.getStage().getOwner(), "Warning!", "The card Not exist in shop :(!");
                    break;
            }
        } catch (NullPointerException e) {
            setUniversalCollectionMenu();
        }
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
        Object cardOrItem = getCardOfItem(id);
        if (buyOrSell) {
            button.setOnMouseClicked(event -> {
                buyID(id);
                money.setText("Money : ".concat(Integer.toString(loginAccount.getMoney())));
            });
            if (cardOrItem instanceof Card) {
                button.setText("Buy " + ((Card) cardOrItem).getName() + " " + ((Card) cardOrItem).getPrice() + "$\n"
                        + Shop.getInstance().getCards().get(cardOrItem) + " of it existed!");
            } else if (cardOrItem instanceof Item)
                button.setText("Buy " + ((Item) cardOrItem).getName() + " " + ((Item) cardOrItem).getPrice() + "$\n " +
                        +Shop.getInstance().getItems().get(cardOrItem) + " of this item existed!");
        } else {
            button.setOnMouseClicked(event -> {
                String name = "";
                if (id.contains("m") || id.contains("h") || id.contains("s")) {
                    name = ((Card) Objects.requireNonNull(getCardOfItem(id))).getName();
                }
                if (id.contains("i")) {
                    name = ((Item) Objects.requireNonNull(getCardOfItem(id))).getName();
                }
                SellRequest sellRequest = new SellRequest(AccountMenu.getLoginAccount().getAuthToken(), Integer.parseInt(id.substring(1)));
                String yaJson1 = yaGson.toJson(sellRequest);
                Client.getWriter().println(yaJson1);
                Client.getWriter().flush();
                cards.remove(name);
                addCardsToContainer(cards.values());
                money.setText("Money : ".concat(Integer.toString(loginAccount.getMoney())));
            });
            if (cardOrItem instanceof Card)
                button.setText("Sell " + ((Card) cardOrItem).getName() + " " + ((Card) cardOrItem).getPrice() + "$");
            else if (cardOrItem instanceof Item)
                button.setText("Sell Item " + ((Item) cardOrItem).getName() + " " + ((Item) cardOrItem).getPrice() + "$");
        }
        ImageView imageView = new ImageView();
        imageView.setFitWidth(splitPane.getPrefWidth());
        Image image = null;
        if (cardOrItem instanceof Item) {
            image = new Image("/resources/cards/general_portrait_image_hex_rook.png");
        } else if (cardOrItem instanceof Card) {
            image = new Image(((Card) cardOrItem).getGraphicPack().getShopPhotoAddress());
        }
        imageView.setImage(image);
        imageView.setOnMouseClicked(event -> {
            CollectionController.removeInfos(infoBox);
            if (cardOrItem instanceof Card) {
                CollectionController.addInfoOfCard((Card) cardOrItem, infoBox);
            } else if (cardOrItem instanceof Item) {
                CollectionController.addItemInfo((Item) cardOrItem, infoBox);
            }
        });
        imageView.setFitHeight(0.82 * splitPane.getPrefHeight());
        splitPane.getItems().add(0, imageView);
        splitPane.getItems().add(1, button);
        if (cardOrItem instanceof Card) {
            cards.put(((Card) cardOrItem).getName(), splitPane);
        } else if (cardOrItem instanceof Item) {
            cards.put(((Item) cardOrItem).getName(), splitPane);
        }
    }

    public void buyID(String id) {
        BuyRequest buyRequest = new BuyRequest(AccountMenu.getLoginAccount().getAuthToken(), Integer.parseInt(id.substring(1)));
        String yaJson1 = yaGson.toJson(buyRequest);
        Client.getWriter().println(yaJson1);
        Client.getWriter().flush();
    }

    private Object getCardOfItem(String id) {
        ArrayList<Object> things = new ArrayList<>(Shop.getInstance().getCards().keySet());
        things.addAll(Shop.getInstance().getItems().keySet());
        for (Object thing : things) {
            if (thing instanceof Card) {
                if (((Card) thing).getCode() == Integer.parseInt(id.substring(1)))
                    return thing;
            } else if (thing instanceof Item) {
                if (((Item) thing).getCode() == Integer.parseInt(id.substring(1)))
                    return thing;
            }
        }
        return null;
    }

    public void addCardsToContainer(Collection<SplitPane> source) {
        topContainer.getChildren().remove(0, topContainer.getChildren().size());
        bottomContainer.getChildren().remove(0, bottomContainer.getChildren().size());
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
        accountCards = loginAccount.getCards();//todo account ..
        accountItems = loginAccount.getItems();//todo account ..
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
        for (Item item : accountItems) {
            String id = "i".concat(Integer.toString(item.getCode()));
            addNewCard(id, false);
        }
        copyCardsInSearchSource();
    }

    public void copyCardsInSearchSource() {
        for (String string : cards.keySet()) {
            forSearchCards.put(string, cards.get(string));
        }
    }

    @FXML
    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }
}
