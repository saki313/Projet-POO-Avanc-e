package controller;

import javafx.application.Platform;
import model.Message;
import model.MessageText;
import model.User;
import network.ChatClientConnection;
import ui.ChatView;
import ui.UserListView;

public class ChatController {
    private ChatClientConnection connection;
    private ChatView chatView;
    private UserListView usersView;

    public ChatController(ChatView view, UserListView usersView) {
        this.connection = new ChatClientConnection();
        this.chatView = view;
        this.usersView = usersView;

        init();
    }

    private void init() {
        // connexion
        connection.connect("localhost", 12345);
        
        // écoute
        connection.setOnMessageReceived(msg -> {
            Platform.runLater(() -> {
                chatView.addMessage(msg);
            });
        });

        // btn envoyer
        chatView.setOnSendAction(e -> {
            MessageText message = chatView.getMessageText();
            connection.send(message);
            chatView.clearInput();
        });

        // usersView.setOnUserSelected(user -> {
        //     loadConversation(user);
        // });
    }

    private void loadConversation(User user) {
        // Load conversation for selected user
    }
}
