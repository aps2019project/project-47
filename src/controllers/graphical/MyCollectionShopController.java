package controllers.graphical;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Shop;
import models.cards.Card;
import models.item.Item;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyCollectionShopController implements Initializable {
    Shop shop;
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Card card : cards) {
            SplitPane splitPane = new SplitPane();
            topContainer.getChildren().add(splitPane);
            splitPane.setPrefHeight(((ScrollPane) splitPane.getParent()).getPrefHeight());
            splitPane.setOrientation(Orientation.VERTICAL);
            splitPane.setDividerPosition(0, 0.82);
            splitPane.setDividerPosition(1, 0.18);
            Button button = new Button("sell" + card.getName());
            splitPane.getItems().add(1, button);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    
                }
            });
            ImageView imageView = new ImageView("resources/generals/general_portrait_image_tutorial1.png");
            splitPane.getItems().add(0, imageView);
        }
        for (Item item : items) {
            SplitPane splitPane = new SplitPane();
            bottomContainer.getChildren().add(splitPane);
            splitPane.setPrefHeight(((ScrollPane) splitPane.getParent()).getPrefHeight());
            splitPane.setDividerPosition(0, 0.82);
            splitPane.setDividerPosition(1, 0.18);
            Label label = new Label(item.getName());
            splitPane.getItems().add(1, label);
            ImageView imageView = new ImageView("resources/generals/general_portrait_image_hex_rook.png");
            splitPane.getItems().add(0, imageView);
        }
    }

    @FXML
    private Button universalCollection;

    @FXML
    private Button myCollection;

    @FXML
    private AnchorPane container;

    @FXML
    private HBox topContainer;

    @FXML
    private HBox bottomContainer;

    @FXML
    void setMyCollectionMenu(ActionEvent event) throws IOException {
        FXMLLoader.load(getClass().getResource("layouts/MyCollectionShop.fxml"));
    }

    @FXML
    void setUniversalCollectionMenu(ActionEvent event) throws IOException {
        FXMLLoader.load(getClass().getResource("layouts/UniversalShop.fxml"));
    }
}
