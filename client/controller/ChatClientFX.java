package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonType;
import javafx.scene.media.AudioClip;
import java.util.Optional;
import java.io.File;
import java.net.URL;

import network.ChatClientConnection;
import model.User;
import model.Message;
import model.MessageText;
import model.MessageFile;
// import ui.chatView1;
import ui.ChatView;
import managerui.*;

public class ChatClientFX extends Application {
    private ChatClientConnection connection;
    private ChatView chatView;
    public static ChatController chatController1;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // ========== Charger les sons ==========
        new Notification().loadSounds();        
        
        // ========== Créer la vue ==========
        chatView = new ChatView();

        chatController1 = new ChatController(chatView, null, primaryStage);
        
        // ========== NOUVEAU : Appliquer le thème ==========
        Theme.applyTheme(chatView);
        
        // 4. Afficher la fenêtre
        primaryStage.setScene(new Scene(chatView, 900, 600));
        primaryStage.show();
        
        // 5. Ajouter un message système
        // chatView.addMessage(new MessageText("Connexion au serveur en cours...", new User("Système")));
    }

    @Override
    public void stop() {
        if (connection != null) {
            connection.closeConnection();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}