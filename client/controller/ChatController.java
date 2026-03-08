package controller;

import java.io.File;
import java.util.Optional;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import model.Message;
import model.MessageFile;
import model.MessageText;
import model.User;
import network.ChatClientConnection;
import ui.ChatView;
import ui.UserListView;
import managerui.XAlert;
import managerui.XConnect;
import managerui.Notification;

public class ChatController {
    private ChatClientConnection connection;
    private ChatView chatView1;
    private UserListView usersView;
    private User user;
    private Stage primaryStage;

     
    // ========== NOUVEAU : Gestion du thème ==========
    private String currentTheme = "sombre"; // clair ou sombre

    public ChatController(ChatView view, UserListView usersView, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.connection = new ChatClientConnection();
        this.user = new User();
        this.chatView1 = view;
        this.usersView = usersView;
        init();
    }

    private void init() {
        showLoginDialog();

        updateTitle("Connexion en cours...");
        
        // Configurer le gestionnaire de messages reçus
        connection.setOnMessageReceived(this::handleIncomingMessage);

        // Configurer l'envoi des messages texte
        chatView1.setOnSendAction(event -> sendMessage());

        // usersView.setOnUserSelected(user -> {
        //     loadConversation(user);
        // });

        // Configurer l'envoi des fichiers
        chatView1.setOnFileSelected(event -> sendFile());
        
        // Configurer le bouton quitter
        chatView1.setOnQuitAction(event -> quitChat());

        // ========== Connexion ==========
        Thread connectionThread = new Thread(() -> {
            try {
                connection.connect("localhost", 12345, user);              
                XConnect.onConnect(chatView1);
            } catch (Exception e) {
                XConnect.onDisconnect(chatView1);
            }
        });
        
        connectionThread.setDaemon(true);
        connectionThread.start();

    }

    private void showLoginDialog() {
        // Demander le nom d'utilisateur
        TextInputDialog dialog = new TextInputDialog("Utilisateur");
        dialog.setHeaderText("Bienvenue sur votre application de discussion instantanée");
        dialog.setContentText("Entrez votre nom d'utilisateur:");
        // dialog.showAndWait().ifPresent(username -> this.user.setName(username));
        
        Optional<String> result = dialog.showAndWait();
        user.setName(result.orElse("Anonymous"));
    }

    private void loadConversation(User user) {
        // Load conversation for selected user
    }
    
    // ========== NOUVEAU : Mettre à jour le titre ==========
    public void updateTitle(String status) {
        Platform.runLater(() -> {
            primaryStage.setTitle("Chat Application - " + user.getName() + " (" + status + ")");
        });
    }
    
    private void handleIncomingMessage(Message message) {
        Platform.runLater(() -> {
            boolean isFromMe = message.getSender().getName().equals(user.getName());
            message.setMine(isFromMe);

            if (message instanceof MessageText textMsg) {                
                if (textMsg.getContent().startsWith("@")) {
                    textMsg.setVisibility("private");
                } else {
                    textMsg.setVisibility("public");
                }
                
                chatView1.addMessage(textMsg);

            } else if (message instanceof MessageFile fileMsg) {
                chatView1.addMessage(fileMsg);
            }
            
            // ========== Jouer un son si le message n'est pas de moi ==========
            if (!isFromMe) {
                Notification.playSound(Notification.notificationSound);
            }
        });
    }
    
    private void sendMessage() {
        MessageText message = chatView1.getMessageText();
        if (message != null) {
            message.setMine(true);
            connection.send(message);
            chatView1.addMessage(message);
            chatView1.clearInput();
        }
    }
    
    private void sendFile() {
        if (chatView1.getSelectedConversation() == null) {
            XAlert.showWarning("Aucune conversation", 
                       "Veuillez sélectionner une conversation avant d'envoyer un fichier.");
            return;
        }
        
        File selectedFile = chatView1.chooseFile();
        if (selectedFile != null) {
            // Vérifier la taille (limite à 10MB)
            if (selectedFile.length() > 10 * 1024 * 1024) {
                XAlert.showWarning("Fichier trop volumineux", 
                           "Le fichier est trop volumineux (max 10MB)");
                return;
            }
            
            // Vérifier le type de fichier (optionnel)
            String fileName = selectedFile.getName();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            
            // Liste des extensions autorisées
            String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "pdf", "txt", "doc", "docx", "mp3", "mp4"};
            boolean allowed = false;
            for (String ext : allowedExtensions) {
                if (ext.equals(extension)) {
                    allowed = true;
                    break;
                }
            }
            
            if (!allowed) {
                XAlert.showWarning("Type de fichier non autorisé", 
                           "Ce type de fichier n'est pas autorisé.");
                return;
            }
            
            MessageFile fileMessage = chatView1.createFileMessage(selectedFile, user);
            
            if (fileMessage != null) {
                connection.send(fileMessage);
                chatView1.addMessage(fileMessage);
            }
        }
    }

    private void quitChat() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Quitter le chat");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment quitter ?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Envoyer un message de déconnexion (optionnel)
            try {
                MessageText quitMsg = new MessageText(user.getName() + " a quitté le chat", new User("Système"));
                connection.send(quitMsg);
            } catch (Exception e) {
                // Ignorer
            }
            
            // Fermer la connexion
            if (connection != null) {
                connection.closeConnection();
            }
            
            // Fermer l'application
            Platform.exit();
        }
    }

    public User getUser() {
        return user;
    }
    
}
