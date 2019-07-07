package controllers.graphical;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import models.Shop;
import models.cards.Card;
import models.item.Item;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ServerShopController implements Initializable {

    public static final String CONTENT_STYLE = "-fx-font-size: 17px;\n" +
            "    -fx-text-fill: brown;\n" +
            "    -fx-font-weight: bold;";

    public GridPane gridPane;

    public static ServerShopController instance;

    {
        instance = this;
    }

    public static HashMap<Card, Label> cardLabelsHashMap = new HashMap<>();
    public static HashMap<Item, Label> itemLabelHashMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeShop();
    }

    private void initializeShop() {
        int i = 1;
        for (Card card : Shop.getInstance().getCards().keySet()) {
            Label nameLabel = new Label(card.getName());
            nameLabel.setStyle(CONTENT_STYLE);
            GridPane.setHalignment(nameLabel, HPos.CENTER);
            Label typeLabel = new Label(card.getCardType().toString());
            typeLabel.setStyle(CONTENT_STYLE);
            GridPane.setHalignment(typeLabel, HPos.CENTER);
            Label priceLabel = new Label(String.valueOf(card.getPrice()));
            priceLabel.setStyle(CONTENT_STYLE);
            GridPane.setHalignment(priceLabel, HPos.CENTER);
            Label inventoryLabel = new Label(String.valueOf(Shop.getInstance().getCards().get(card)));
            inventoryLabel.setStyle(CONTENT_STYLE);
            GridPane.setHalignment(inventoryLabel, HPos.CENTER);
            cardLabelsHashMap.put(card, inventoryLabel);
            gridPane.add(nameLabel, 0, i);
            gridPane.add(typeLabel, 1, i);
            gridPane.add(priceLabel, 2, i);
            gridPane.add(inventoryLabel, 3, i);
            i++;
        }
        for (Item item : Shop.getInstance().getItems().keySet()) {
            Label nameLabel = new Label(item.getName());
            Label typeLabel = new Label("Item");
            Label priceLabel = new Label(String.valueOf(item.getPrice()));
            Label inventoryLabel = new Label(String.valueOf(Shop.getInstance().getItems().get(item)));
            typeLabel.setStyle(CONTENT_STYLE);
            nameLabel.setStyle(CONTENT_STYLE);
            priceLabel.setStyle(CONTENT_STYLE);
            inventoryLabel.setStyle(CONTENT_STYLE);
            GridPane.setHalignment(nameLabel, HPos.CENTER);
            GridPane.setHalignment(typeLabel, HPos.CENTER);
            GridPane.setHalignment(priceLabel, HPos.CENTER);
            GridPane.setHalignment(inventoryLabel, HPos.CENTER);
            gridPane.add(nameLabel, 0, i);
            gridPane.add(typeLabel, 1, i);
            gridPane.add(priceLabel, 2, i);
            gridPane.add(inventoryLabel, 3, i);
            itemLabelHashMap.put(item, inventoryLabel);
            i++;
        }
    }

    public void updateTable() {
        for (Card card : Shop.getInstance().getCards().keySet())
            Platform.runLater(() -> cardLabelsHashMap.get(card).setText(String.valueOf(Shop.getInstance().getCards().get(card))));
        for (Item item : Shop.getInstance().getItems().keySet())
            Platform.runLater(() -> itemLabelHashMap.get(item).setText(String.valueOf(Shop.getInstance().getItems().get(item))));
    }
}
