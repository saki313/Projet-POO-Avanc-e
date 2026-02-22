package client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
public class ChatClientFX extends Application {

    // --- DÉCLARATION DES VARIABLES (Champs de la classe) ---
    private TextArea messageArea;
    private TextField textField;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // --- 1. CRÉATION DE L'INTERFACE ---
        primaryStage.setTitle("Mon Chat JavaFX - Client");

        // Zone d'affichage des messages
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        VBox.setVgrow(messageArea, Priority.ALWAYS); 

        // Zone de saisie (Bas)
        textField = new TextField();
        textField.setPromptText("Écrivez votre message ici...");
        HBox.setHgrow(textField, Priority.ALWAYS);

        Button sendButton = new Button("Envoyer");
        Button fileButton = new Button("📎");
        fileButton.setTooltip(new Tooltip("Partager un fichier"));

        HBox bottomPanel = new HBox(10); 
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.getChildren().addAll(fileButton, textField, sendButton);

        // Assemblage dans le conteneur principal
        VBox root = new VBox(5);
        root.getChildren().addAll(messageArea, bottomPanel);

        // --- 2. ACTIONS ---
        sendButton.setOnAction(e -> sendMessage());
        textField.setOnAction(e -> sendMessage()); // Touche Entrée
        fileButton.setOnAction(e -> selectAndSendFile(primaryStage));

        // --- 3. SCÈNE ET AFFICHAGE ---
        Scene scene = new Scene(root, 550, 450);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Lancement de la connexion
        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                // Remplacez "localhost" par l'IP du serveur si nécessaire
                socket = new Socket("localhost", 12345);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String serverMsg;
                while ((serverMsg = in.readLine()) != null) {
                    final String msg = serverMsg;
                    Platform.runLater(() -> {
                        messageArea.appendText(msg + "\n");
                    });
                }
            } catch (IOException e) {
                Platform.runLater(() -> 
                    messageArea.appendText("!!! Erreur : Connexion au serveur impossible !!!\n")
                );
            }
        }).start();
    }

    private void sendMessage() {
        String msg = textField.getText().trim();
        if (!msg.isEmpty()) {
            if (out != null) {
                out.println(msg);
                textField.clear();
            } else {
                messageArea.appendText("Système : Vous n'êtes pas connecté au serveur.\n");
            }
        }
    }

    private void selectAndSendFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier à partager");
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null && out != null) {
            String fileInfo = "📁 [PARTAGE DE FICHIER] : " + selectedFile.getName() 
                            + " (Taille: " + (selectedFile.length() / 1024) + " KB)";
            out.println(fileInfo);
            messageArea.appendText("Local : Fichier sélectionné -> " + selectedFile.getName() + "\n");
        }
    }

    @Override
    public void stop() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}