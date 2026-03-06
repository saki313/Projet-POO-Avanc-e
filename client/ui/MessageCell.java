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
        VBox container = new VBox(5);
        container.setPadding(new Insets(5));
        
        Label sender = new Label(message.getSender().getName());
        sender.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");
        
        Label contentLabel = new Label(message.getContent());
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(300);
        
        String timeStr = String.format("%02d:%02d", 
            message.getTimestamp().getHour(), 
            message.getTimestamp().getMinute());
        Label hour = new Label(timeStr);
        hour.setStyle("-fx-font-size: 8px; -fx-text-fill: gray;");

        container.getChildren().addAll(sender, contentLabel, hour);
        container.setAlignment(message.isMine() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        contentLabel.setPadding(new Insets(8, 12, 8, 12));
        
        // Style de bulle amélioré
        if (message.isMine()) {
            contentLabel.setStyle("-fx-background-color: #0078FF; -fx-text-fill: white; " +
                "-fx-background-radius: 15 15 0 15; -fx-font-size: 14px;");
            container.setAlignment(Pos.CENTER_RIGHT);
        } else {
            contentLabel.setStyle("-fx-background-color: #E9E9EB; -fx-text-fill: black; " +
                "-fx-background-radius: 15 15 15 0; -fx-font-size: 14px;");
            container.setAlignment(Pos.CENTER_LEFT);
        }

        setGraphic(container);
    }
   
    private void showFileMessage(MessageFile message) {
        VBox container = new VBox(5);
        container.setPadding(new Insets(5));
        
        Label sender = new Label(message.getSender().getName());
        sender.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");
        
        HBox fileBox = new HBox(10);
        fileBox.setAlignment(Pos.CENTER_LEFT);
        
        Label fileName = new Label("📄 " + message.getFileName());
        fileName.setStyle("-fx-font-size: 14px;");
        
        Button downloadButton = new Button("⬇ Télécharger");
        downloadButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        // Action de téléchargement
        downloadButton.setOnAction(e -> {
            // TODO: Implémenter le téléchargement du fichier
            System.out.println("Téléchargement de: " + message.getFileName());
        });
        
        fileBox.getChildren().addAll(fileName, downloadButton);
        
        String timeStr = String.format("%02d:%02d", 
            message.getTimestamp().getHour(), 
            message.getTimestamp().getMinute());
        Label hour = new Label(timeStr);
        hour.setStyle("-fx-font-size: 8px; -fx-text-fill: gray;");
        
        container.getChildren().addAll(sender, fileBox, hour);
        container.setAlignment(message.isMine() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        // Style pour les fichiers
        if (message.isMine()) {
            container.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 10;");
        } else {
            container.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 10;");
        }

        setGraphic(container);
    }
}