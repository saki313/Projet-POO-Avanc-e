import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.ChatController;
import ui.ChatView;
public class ChatClientFX extends Application { 
    @Override
    public void start(Stage primaryStage) throws Exception {
        // --- 1. CRÉATION DE LA VUE ---
        ChatView view = new ChatView();
        Scene scene = new Scene(view.getRoot(), 550, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Client");
        
        // --- 2. CONTRÔLEUR ---
        new ChatController(view);

         // --- 3. SCÈNE ET AFFICHAGE ---
         primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}