package controllers.graphical;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import models.battle.GraveYard;
import models.battle.Player;
import models.cards.Card;


public class GraveYardController {
    private Player player1;
    private Player player2;

    @FXML
    private SplitPane parentOfAll;

    @FXML
    private AnchorPane leftAnchorPane;

    @FXML
    private ScrollPane leftScrollPane;

    @FXML
    private GridPane leftGrid;

    @FXML
    private AnchorPane rightAnchorPane;

    @FXML
    private ScrollPane rightScrollPane;

    @FXML
    private GridPane rightGrid;

    @FXML
    private Button backButton;

    @FXML
    private void back(){

    }
    public SplitPane createCard(Card card){
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setPrefWidth(leftGrid.getLayoutX() / 3);
        splitPane.setDividerPositions(0.90);
        Image image = new Image(card.getGraphicPack().getShopPhotoAddress());
        ImageView imageView = new ImageView(image);
        Label label = new Label(card.getName());
        imageView.setFitHeight(leftGrid.getLayoutX() / 3);
        imageView.setFitWidth(leftGrid.getLayoutX() / 3);
        label.setPrefWidth(leftGrid.getLayoutX() / 3);
        label.setPrefHeight(leftGrid.getLayoutX() / 27);
        label.setStyle("-fx-background-radius: 100; " +
                "-fx-background-color: #ce4534;" +
                "-fx-font-weight: 900;" +
                "-fx-font-size: 17;");
        splitPane.getItems().add(0, imageView);
        splitPane.getItems().add(1, label);
        return splitPane;
    }
    public void addCardsToGraveYard(Player player, boolean right){
        GridPane gridPane;
        int row = 0;
        int column = 0;
        if (right)
            gridPane = rightGrid;

        else gridPane = leftGrid;
        for(Card card : player.getGraveYard().getCards()){
            SplitPane splitPane = createCard(card);
            gridPane.add(splitPane, column, row/3);
            column = (column + 1)%3;
            row ++;
        }
    }
    public void update(Player player1, Player player2){
        addCardsToGraveYard(player1, false);
        addCardsToGraveYard(player2, true);
    }
}
