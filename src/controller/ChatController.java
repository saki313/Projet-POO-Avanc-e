package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import network.ChatClient;
import model.Message;

public class ChatController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    private ChatClient client;

    @FXML
    public void initialize() {
        client = new ChatClient(...);
    }

    @FXML
    private void handleSend() {
        String text = messageField.getText();
        client.sendMessage(text);
        messageField.clear();
    }

    public void receiveMessage(Message msg) {
        Platform.runLater(() -> {
            chatArea.appendText(msg.getContent() + "\n");
        });
    }
}