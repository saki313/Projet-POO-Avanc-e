package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.ChatView;
import managerui.*;

public class ChatClientFX extends Application {
    private ChatView chatView;
    public static ChatController chatController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Notification().loadSounds();
        
        chatView = new ChatView();
        chatController = new ChatController(chatView, primaryStage);
        
        Scene scene = new Scene(chatView, 900, 600);
        primaryStage.setScene(scene);
        
        Theme.setScene(scene);
        Theme.apply("light");
        
        primaryStage.show();
    }

    @Override
    public void stop() {
        chatController.quitChat();
    }

    public static void main(String[] args) {
        launch(args);
    }
}