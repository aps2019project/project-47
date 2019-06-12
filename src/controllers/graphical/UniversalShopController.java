package controllers.graphical;

import com.jfoenix.controls.JFXButton;
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
import models.Shop;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UniversalShopController implements Initializable {
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

    }

    @FXML
    void setUniversalCollectionMenu(ActionEvent event) {

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
            addNewCard(id);
        for (String id : heroIds)
            addNewCard(id);
        for (String id : spellIds)
            addNewCard(id);
        for (String id : itemIds)
            addNewCard(id);
    }

    private void addNewCard(String id) {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setDividerPosition(0, 0.82);
        splitPane.setDividerPosition(1, 0.18);
        splitPane.setPrefHeight(topContainer.getPrefHeight() - 20);
        splitPane.setPrefWidth(splitPane.getPrefHeight() * 0.82);
        JFXButton button = new JFXButton("Buy");
        button.setId(id);
        button.setPrefWidth(splitPane.getPrefWidth());
        button.setPrefHeight(0.18 * splitPane.getPrefHeight());
        button.setOnAction(event -> shop.command_buy(Integer.parseInt(id.substring(1))));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(splitPane.getPrefWidth());
        Image image = new Image("/resources/general_portrait_image_hex_rook.png");
        imageView.setImage(image);
        imageView.setFitHeight(0.82 * splitPane.getPrefHeight());
        splitPane.getItems().add(0, imageView);
        splitPane.getItems().add(1, button);
        cards.add(splitPane);
    }

    public void addCardsToContainer(ArrayList<SplitPane> source){
        for (SplitPane s : source){
            String id = s.getItems().get(1).getId();
            if (id.contains("m") || id.contains("h")){
                topContainer.getChildren().add(s);
            }
            else if (id.contains("i") || id.contains("s")){
                bottomContainer.getChildren().add(s);
            }
        }
    }
}
