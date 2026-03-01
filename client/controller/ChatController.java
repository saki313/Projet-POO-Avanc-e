package controller;

import javafx.application.Platform;
import network.ChatClientConnection;
import ui.ChatView;

public class ChatController {
    private ChatView view;
    private ChatClientConnection connection;

    public ChatController(ChatView view) {
        this.view = view;
        this.connection = new ChatClientConnection();
        init();
    }

    private void init() {
        // connexion
        connection.connect("localhost", 12345);
        
        // écoute
        connection.setOnMessageReceived(msg -> {
            Platform.runLater(() -> {
                view.appendMessage(msg);
            });
        });

        // btn envoyer
        view.setOnSendAction(e -> {
            String message = view.getMessage();
            connection.send(message);
            view.clearInput();
        });
    }
}
