package controllers.graphical;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UniversalShopController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topContainer.setPrefHeight(((ScrollPane)topContainer.getParent()).getPrefHeight());
        bottomContainer.setPrefHeight(((ScrollPane)topContainer.getParent()).getPrefHeight());
    }

    @FXML
    private HBox topContainer;

    @FXML
    private HBox bottomContainer;

    @FXML
    private Button universalCollection;

    @FXML
    private Button myCollection;

    @FXML
    private AnchorPane container;

    @FXML
    private SplitPane heroesAndMinions;

    @FXML
    private Button m201;

    @FXML
    private Button m202;

    @FXML
    private Button m203;

    @FXML
    private Button m204;

    @FXML
    private Button m205;

    @FXML
    private Button m206;

    @FXML
    private Button m207;

    @FXML
    private Button m208;

    @FXML
    private Button m209;

    @FXML
    private Button m210;

    @FXML
    private Button m211;

    @FXML
    private Button m212;

    @FXML
    private Button m213;

    @FXML
    private Button m214;

    @FXML
    private Button m215;

    @FXML
    private Button m216;

    @FXML
    private Button m217;

    @FXML
    private Button m218;

    @FXML
    private Button m219;

    @FXML
    private Button m220;

    @FXML
    private Button m221;

    @FXML
    private Button m222;

    @FXML
    private Button m223;

    @FXML
    private Button m224;

    @FXML
    private Button m225;

    @FXML
    private Button m226;

    @FXML
    private Button m227;

    @FXML
    private Button m228;

    @FXML
    private Button m229;

    @FXML
    private Button m230;

    @FXML
    private Button m231;

    @FXML
    private Button m232;

    @FXML
    private Button m233;

    @FXML
    private Button m234;

    @FXML
    private Button m235;

    @FXML
    private Button m236;

    @FXML
    private Button m237;

    @FXML
    private Button m238;

    @FXML
    private Button m239;

    @FXML
    private Button m240;

    @FXML
    private Button h301;

    @FXML
    private Button h302;

    @FXML
    private Button h303;

    @FXML
    private Button h304;

    @FXML
    private Button h305;

    @FXML
    private Button h306;

    @FXML
    private Button h307;

    @FXML
    private Button h308;

    @FXML
    private Button h309;

    @FXML
    private Button h310;

    @FXML
    void buyCard(ActionEvent event) {
        String codeStr = ((Button)event.getSource()).getId();
        Integer code = Integer.parseInt(codeStr.substring(1));

    }

    @FXML
    void setMyCollectionMenu(ActionEvent event) {

    }

    @FXML
    void setUniversalCollectionMenu(ActionEvent event) throws IOException {
        FXMLLoader.load(getClass().getResource("layouts/UniversalShop.fxml"));
    }
}
