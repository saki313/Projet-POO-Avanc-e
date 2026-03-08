package managerui;

import controller.ChatClientFX;
import javafx.application.Platform;
import model.MessageText;
import model.User;
import ui.ChatView;

public class XConnect {

    public static void onConnect(ChatView chatView1 ) {
        Platform.runLater(() -> {
            Notification.playSound(Notification.connectSound);
            chatView1.addMessage(new MessageText("✅ Connecté au serveur !", new User("Système")));
            chatView1.setConnectionStatus(true, 1); // Mettre à jour la barre de statut
            ChatClientFX.chatController1.updateTitle("Connecté");
        });  
    }

    public static void onDisconnect(ChatView chatView1) {
        Platform.runLater(() -> {
            XAlert.showError("Erreur de connexion", 
                        "Impossible de se connecter au serveur.\nVérifiez que le serveur est démarré.");
            chatView1.addMessage(new MessageText("❌ Échec de connexion au serveur", new User("Système")));
            chatView1.setConnectionStatus(false, 0);
            ChatClientFX.chatController1.updateTitle("Déconnecté");
        });
    }
}