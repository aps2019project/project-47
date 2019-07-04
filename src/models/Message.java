package models;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {
    private String senderUserName;
    private String textMessage;
    private String time;
    private String messageId;
    public Message(String senderUserName, String textMessage) {
        this.senderUserName = senderUserName;
        this.textMessage = textMessage;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        this.time = dateFormat.format(date);
        this.messageId = Account.generateRandomString(64);
    }

    public AnchorPane buildMessageBox(boolean mine) {
        AnchorPane box = new AnchorPane();
        box.setPrefWidth(350);
        box.setPrefHeight(200);
        if (mine) box.setStyle(
                    "-fx-background-radius: 20px 20px 0px 20px; " +
                    "-fx-background-color:  linear-gradient(to right, #2980b9, #2c3e50);");

        else box.setStyle(
                    "-fx-background-radius: 20px 20px 20px 0px; " +
                    "-fx-background-color:  linear-gradient(to right, #757f9a, #d7dde8);");
        Label sender;
        if (mine) {
            sender = new Label(this.senderUserName);
        }
        else
            sender = new Label("Me");
        Font font = Font.font("System", FontWeight.BOLD, 13);
        sender.setFont(font);
        Label time = new Label(this.time);
        time.setFont(font);
        AnchorPane messageBox = new AnchorPane();
        Label message = new Label(this.textMessage);
        if (mine){
            message.setTextFill(Color.web("#DBDBDB"));
            sender.setTextFill(Color.web("#DBDBDB"));
            time.setTextFill(Color.web("#DBDBDB"));
        }
        message.setWrapText(true);
        message.setTextAlignment(TextAlignment.JUSTIFY);
        Font font1 = Font.font("System", FontWeight.EXTRA_BOLD, 15);
        message.setFont(font1);
        box.getChildren().add(sender);
        messageBox.getChildren().add(message);
        box.getChildren().add(messageBox);
        box.getChildren().add(time);
        messageBox.setPrefWidth(((AnchorPane) messageBox.getParent()).getPrefWidth() - 20);
        message.setPrefWidth(((AnchorPane) message.getParent()).getPrefWidth() - 20);
        AnchorPane.setLeftAnchor(sender, 10d);
        AnchorPane.setLeftAnchor(messageBox, 10d);
        AnchorPane.setLeftAnchor(message, 10d);
        AnchorPane.setRightAnchor(time, 10d);
        AnchorPane.setTopAnchor(sender, 10d);
        AnchorPane.setTopAnchor(messageBox, 30d);
        AnchorPane.setBottomAnchor(messageBox, 30d);
        AnchorPane.setBottomAnchor(time, 10d);
        return box;
    }
}
