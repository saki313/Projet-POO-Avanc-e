import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import controller.ChatController;
import ui.ChatView;
import ui.UserListView;
public class ChatClientFX extends Application { 
    @Override
    public void start(Stage primaryStage) throws Exception {
    // --- 1. CRÉATION DE LA VUE ---
        BorderPane root = new BorderPane();
        ChatView chatView = new ChatView();
        UserListView usersView = new UserListView();

        root.setLeft(usersView.getRoot());
        root.setCenter(chatView);

        Scene scene = new Scene(root, 550, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Client");
        
    // --- 2. CONTRÔLEUR ---
        new ChatController(chatView, usersView);

    // --- 3. SCÈNE ET AFFICHAGE ---
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}