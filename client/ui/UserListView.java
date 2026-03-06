package ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import model.User;

public class UserListView extends BorderPane {
    private BorderPane root;
    private ListView<String> userList;

    public UserListView() {
        root = new BorderPane();
        Label userLabel = new Label("Utilisateurs connectés");
        userLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10;");
        
        userList = new ListView<>();
        userList.getItems().addAll("Client 1", "Client 2");
        userList.setStyle("-fx-control-inner-background: #81b4b4");

        root.setTop(userLabel);
        root.setCenter(userList);
    }

    public BorderPane getRoot() {
        return root;
    }

    public void addUser(String username) {
        userList.getItems().add(username);
    }

    public void removeUser(String username) {
        userList.getItems().remove(username);
    }

    public void setOnUserSelected(java.util.function.Consumer<String> handler) {
        userList.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    handler.accept(newVal);
                }
            }
        );
    }
}