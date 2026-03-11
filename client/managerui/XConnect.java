package managerui;

import controller.ChatClientFX;
import javafx.application.Platform;
import ui.ChatView;

public class XConnect {

    public static void onConnect(ChatView chatView) {
        Platform.runLater(() -> {
            Notification.playSound(Notification.connectSound);
            chatView.setConnectionStatus(true, 0/* chatView.getUserListView().getUsers().size() */); // Mettre à jour la barre de statut
            ChatClientFX.chatController.updateTitle("Connecté");
        });  
    }

    public static void onDisconnect(ChatView chatView) {
        Platform.runLater(() -> {
            XAlert.showError("Erreur de connexion", 
                        "Impossible de se connecter au serveur.\nVérifiez que le serveur est démarré.");
            chatView.setConnectionStatus(false, 0/* chatView.getUserListView().getUsers().size() */); // Mettre à jour la barre de statut
            ChatClientFX.chatController.updateTitle("Déconnecté");
        });
    }
}