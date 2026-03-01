package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Message;
import model.MessageFile;
import model.MessageText;

public class MessageCell extends ListCell<Message> {

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        
        if (message instanceof MessageText) {
            showTextMessage((MessageText) message);
        } else if (message instanceof MessageFile) {
            showFileMessage((MessageFile) message);
        } else {
            setText("Message inconnu");
        }
    }
    
    private void showTextMessage(MessageText message) {
        VBox container = new VBox();
        Label sender = new Label(message.getSender().getUsername());
        Label contentLabel = new Label(message.getContent());
        Label hour = new Label(message.getTimestamp().toString().substring(0, 5));

        container.getChildren().addAll(sender, contentLabel, hour);
        container.setAlignment(message.isMine() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        contentLabel.setPadding(new Insets(5, 10, 5, 10));
        contentLabel.setStyle(message.isMine() ? "-fx-background-color: #0078FF; -fx-text-fill: white;" 
        : "-fx-background-color: #E9E9EB; -fx-text-fill: black;");

        setGraphic(container);
    }
   
    private void showFileMessage(MessageFile message) {
        HBox container = new HBox();
        Label sender = new Label(message.getSender().getUsername());
        Label fileName = new Label(message.getFileName());
        Button downloadButton = new Button("Download");
        // TODO "append an icon on the button"

        container.getChildren().addAll(sender, fileName, downloadButton);
        container.setAlignment(message.isMine() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        setGraphic(container);
    }

    // TODO "design visuel avancé des bulles" "gestion du téléchargement" "auto-scroll intelligent quand un message arrive"
}   
