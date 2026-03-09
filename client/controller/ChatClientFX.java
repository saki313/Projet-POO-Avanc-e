package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.ChatClientConnection;
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

        chatController1 = new ChatController(chatView, primaryStage);
        
        // ========== Appliquer le thème ==========
        Theme.setScene(primaryStage.getScene());
        Theme.apply("dark");
        
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