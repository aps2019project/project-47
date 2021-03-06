package controllers.graphical;

import com.gilecode.yagson.YaGson;
import com.jfoenix.controls.JFXTextField;
import controllers.console.AccountMenu;
import controllers.console.MainMenu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import models.Account;
import models.Message;
import network.Client;
import network.Requests.chatRoom.LeaveChatRequest;
import network.Requests.chatRoom.SendMessageRequest;
import network.Requests.chatRoom.UpdateChatRequest;


import java.net.URL;
import java.util.ResourceBundle;

public class GlobalChatController implements Initializable {
    Account loginAccount = AccountMenu.getLoginAccount();
    YaGson yaGson = new YaGson();

    public static GlobalChatController instance;

    {
        instance = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("/resources/backgrounds/sendImage.png");
        Background background = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(
                100, 100, true, true, true, true)));
        sendButton.setBackground(background);

        chatText.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                sendMessage();
        });
        update();
    }

    private void update() {
        UpdateChatRequest request = new UpdateChatRequest(loginAccount.getAuthToken());
        Client.getWriter().println(yaGson.toJson(request));
        Client.getWriter().flush();
    }

    public void addNewMessage(Message message) {
        if (message.getSenderUserName().equals(loginAccount.getUserName())) {
            AnchorPane myMessage = message.buildMessageBox(true);
            chats.getChildren().add(myMessage);
            VBox.setMargin(myMessage, new Insets(5, 20, 5, 300));
        } else {
            AnchorPane theirMessage = message.buildMessageBox(false);
            chats.getChildren().add(theirMessage);
            VBox.setMargin(theirMessage, new Insets(5, 300, 5, 20));
        }
    }

    @FXML
    public JFXTextField chatText;

    @FXML
    public VBox chats;

    @FXML
    public ScrollPane chatsScrollPane;

    @FXML
    private Button sendButton;

    @FXML
    private Button backButton;

    @FXML
    private void sendMessage() {
        String senderUserName = loginAccount.getUserName();
        String textMessage = chatText.getText();
        if (textMessage == null || textMessage.equals("")) {
            return;
        }

        Message message = new Message(senderUserName, textMessage);
        SendMessageRequest request = new SendMessageRequest(message, loginAccount.getAuthToken());
        Client.getWriter().println(yaGson.toJson(request));
        Client.getWriter().flush();
        chatText.clear();
    }

    @FXML
    private void back() {
        LeaveChatRequest leaveChatRequest = new LeaveChatRequest(loginAccount.getAuthToken());
        Client.getWriter().println(yaGson.toJson(leaveChatRequest));
        Client.getWriter().flush();
    }

    public void goToMainMenu() {
        Client.getStage().getScene().setRoot(MainMenu.getRoot());
    }
}
