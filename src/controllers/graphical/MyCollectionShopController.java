package controllers.graphical;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.cards.Card;
import models.item.Item;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyCollectionShopController implements Initializable {
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Card card : cards){
            SplitPane splitPane = new SplitPane();
            topContainer.getChildren().add(splitPane);
            splitPane.setPrefHeight(((ScrollPane)splitPane.getParent()).getPrefHeight());
            splitPane.setOrientation(Orientation.VERTICAL);
            splitPane.setDividerPosition(0, 0.82);
            splitPane.setDividerPosition(1, 0.18);
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
