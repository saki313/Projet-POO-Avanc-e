package ui;

import java.time.LocalTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import model.Message;
import model.MessageText;
import model.User;

public class ChatView extends BorderPane {

    private ListView<Message> messageListView;
    private ObservableList<Message> messages;  // Fixed typo

    private TextArea messageArea;
    private TextField inputField;

    private Button sendButton;
    private Button fileButton;

    public ChatView() {
        // Construction UI

        // ListView et Message zone V2
        messages = FXCollections.observableArrayList();

        messageListView = new ListView<>(messages);
        messageListView.setCellFactory(list -> new MessageCell());

        // Zone de saisie (Bas)
        inputField = new TextField();
        inputField.setPromptText("Entrez un message...");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        sendButton = new Button("Envoyer");
        fileButton = new Button("📎");
        fileButton.setTooltip(new Tooltip("Partager un fichier"));

        HBox bottomPanel = new HBox(10); 
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.getChildren().addAll(fileButton, inputField, sendButton);

        // Assemblage
        this.setCenter(messageListView);  // Changed from messageArea to messageListView
        this.setBottom(bottomPanel);
    }

    public MessageText getMessageText() {
        return new MessageText(inputField.getText().trim(), new User("Amed")); // TODO: getSender() à implémenter
    }

    public void clearInput() {
        inputField.clear();
    }

    public void addMessage(Message msg) {
        messages.add(msg); // Ajoute le message à la liste observable
        // Auto-scroll to bottom
        messageListView.scrollTo(messages.size() - 1);
    }

    public void setOnSendAction(EventHandler<ActionEvent> handler) {
        sendButton.setOnAction(handler);
        inputField.setOnAction(handler); // Permet d'envoyer avec la touche Entrée
    }

    public void setOnFileAction(EventHandler<ActionEvent> handler) {
        fileButton.setOnAction(handler);
    }

    public void setOnSendMessage(Message object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOnSendMessage'");
    }
}