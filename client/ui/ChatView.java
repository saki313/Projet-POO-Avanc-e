package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ChatView {
    private BorderPane root;
    private TextArea messageArea;
    private TextField textField;
    private Button sendButton;
    private Button fileButton;

    public ChatView() {
    // Construction UI
        root = new BorderPane();

    // Zone d'affichage des messages
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setStyle("-fx-control-inner-background: #1e1e1e; -fx-font-size: 14px; -fx-text-fill: #dcdcdc;");
        VBox.setVgrow(messageArea, Priority.ALWAYS); 

        // Zone de saisie (Bas)
        textField = new TextField();
        textField.setPromptText("Entrez un message...");
        HBox.setHgrow(textField, Priority.ALWAYS);

        sendButton = new Button("Envoyer");
        fileButton = new Button("📎");
        fileButton.setTooltip(new Tooltip("Partager un fichier"));

        HBox bottomPanel = new HBox(10); 
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.getChildren().addAll(fileButton, textField, sendButton);

        // Assemblage
        BorderPane rootMZ = new BorderPane();
        rootMZ.setCenter(messageArea);
        rootMZ.setBottom(bottomPanel);

    // Liste des utilisateurs
        Label userLabel = new Label("Utilisateurs connectés");
        ListView<String> userList = new ListView<String>();
        userList.setStyle("-fx-control-inner-background: #81b4b4");
        BorderPane rootUS = new BorderPane();
        rootUS.setTop(userLabel);
        rootUS.setCenter(userList);
        // rootUS.setStyle("-fx-background: #1e1e1e; -fx-border-color: #333; -fx-border-width: 1px; -fx-padding: 10px;");

        root.setLeft(rootUS);
        root.setCenter(rootMZ);
    }

    public BorderPane getRoot() {
        return root;
    }

    public String getMessage() {
        return textField.getText().trim();
    }

    public void clearInput() {
        textField.clear();
    }

    public void appendMessage(String msg) {
        messageArea.appendText(msg + "\n");
    }

    public void setOnSendAction(EventHandler<ActionEvent> handler) {
        sendButton.setOnAction(handler);
        textField.setOnAction(handler); // Permet d'envoyer avec la touche Entrée
    }

    public void setOnFileAction(EventHandler<ActionEvent> handler) {
        fileButton.setOnAction(handler);
    }
    
}
