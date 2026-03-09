package controller;

import java.io.File;
import java.util.Optional;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
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
    private ChatView chatView;
    private UserListView usersView;
    private User user;
    private Stage primaryStage;
    
    private String ipAddress;
    private int port;

    

     
    // ========== Gestion du thème ==========
    private String currentTheme = "sombre"; // clair ou sombre

    public ChatController(ChatView view, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.connection = new ChatClientConnection();
        this.user = new User();
        this.chatView = view;
        this.usersView = chatView.getUserListView();
        init();
    }

    private void init() {
        showLoginDialog();
        updateTitle("Connexion en cours...");
        showConnectionDialog();

        chatView.ensurePublicGroupExists();
        
        // Configurer le gestionnaire de messages reçus
        connection.setOnMessageReceived(this::handleIncomingMessage);
        chatView.getUserListView().setOnUserSelected(this::handleUserSelected);
        // Configurer l'envoi des messages texte
        chatView.setOnSendAction(event -> sendMessage());

        // usersView.setOnUserSelected(user -> {
        //     loadConversation(user);
        // });

        // Configurer l'envoi des fichiers
        chatView.setOnFileSelected(event -> sendFile());
        
        // Configurer le bouton quitter
        chatView.setOnQuitAction(event -> quitChat());

        // ========== Connexion ==========
        Thread connectionThread = new Thread(() -> {
            try {
                connection.connect(ipAddress, port, user);              
                XConnect.onConnect(chatView);
            } catch (Exception e) {
                XConnect.onDisconnect(chatView);
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

    private void showConnectionDialog() {
        TextInputDialog dialogAddress = new TextInputDialog("localhost");
        TextInputDialog dialogPort = new TextInputDialog("12345");

        dialogAddress.setContentText("Entrez l'adresse ip du serveur:");
        dialogPort.setContentText("Entrez le port sur lequel est connecté votre serveur:");

        ipAddress = dialogAddress.showAndWait().orElse("localhost");        
        port = Integer.valueOf(dialogPort.showAndWait().orElse("12345")).intValue();  
      
    }

    private void loadConversation(User user) {
        // Load conversation for selected user
    }
    
    // ========== Mettre à jour le titre ==========
    public void updateTitle(String status) {
        Platform.runLater(() -> {
            primaryStage.setTitle("Chat Application - " + user.getName() + " (" + status + ")");
        });
    }
    
    private void handleIncomingMessage(Message message) {
        Platform.runLater(() -> {
            boolean isFromMe = message.getSender().getName().equals(user.getName());
            message.setMine(isFromMe);

            String conversationId = null;
            if (message instanceof MessageText textMsg) {                
                if (textMsg.getVisibility().equals("private")) {
                    if (!isFromMe) {
                        conversationId = "private_" + message.getSender().getName();
                    } else {
                        // Message envoyé par moi : la conversation courante est déjà définie
                        conversationId = chatView.getCurrentConversation() != null ? chatView.getCurrentConversation().getId() : null;
                    }

                    // textMsg.setVisibility("private");
                } else {
                    // Message public : groupe public
                    conversationId = "public_group";
                    // textMsg.setVisibility("public");
                }
                
                // chatView.addMessage(textMsg);

            } else if (message instanceof MessageFile fileMsg) {
                if (fileMsg.getVisibility().equals("private")) {
                    if (!isFromMe) {
                        conversationId = "private_" + message.getSender().getName();
                    } else {
                        // Message envoyé par moi : la conversation courante est déjà définie
                        conversationId = chatView.getCurrentConversation() != null ? chatView.getCurrentConversation().getId() : null;
                    }

                    // textMsg.setVisibility("private");
                } else {
                    // Message public : groupe public
                    conversationId = "public_group";
                    // textMsg.setVisibility("public");
                }    
            }
            
            if (conversationId != null) {
                // Vérifier que la conversation existe dans la liste des conversations, sinon la créer

                ChatView.ConversationItem conv = chatView.getOrCreatePrivateConversation(conversationId, message.getSender());
                chatView.addMessageToConversation(conversationId, message);

            }

            // ========== Jouer un son si le message n'est pas de moi ==========
            if (!isFromMe) {
                Notification.playSound(Notification.notificationSound);
            }
        });
    }
    
    private void sendMessage() {
        MessageText message = chatView.getMessageText();
        if (message != null) {
            message.setMine(true);
            message.setSender(user);
            connection.send(message);
            chatView.addMessage(message);
            chatView.clearInput();
        }
    }
    
    private void sendFile() {
        if (chatView.getSelectedConversation() == null) {
            XAlert.showWarning("Aucune conversation", 
                       "Veuillez sélectionner une conversation avant d'envoyer un fichier.");
            return;
        }
        
        File selectedFile = chatView.chooseFile();
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
            
            MessageFile fileMessage = chatView.createFileMessage(selectedFile, user);
            
            if (fileMessage != null) {
                connection.send(fileMessage);
                chatView.addMessage(fileMessage);
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

    // Ajouter la méthode handleUserSelected (qui était commentée) :
    private void handleUserSelected(User selectedUser) {
        // La vue gère déjà la création de la conversation, donc on n'a rien à faire ici ?
        // En fait, la vue crée la conversation et la sélectionne. On peut juste notifier.
        // Mais si on veut charger l'historique, on peut le faire.
    }

    public User getUser() {
        return user;
    }
    
}
