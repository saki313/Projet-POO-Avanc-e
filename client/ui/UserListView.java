package ui;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import model.User;

public class UserListView extends BorderPane {
    private BorderPane root;

    public UserListView() {
        root = new BorderPane();
        Label userLabel = new Label("Utilisateurs connectés");
        ListView<String> userList = new ListView<>();
        
        userList.getItems().addAll("Client 1","Client 2");
        userList.setStyle("-fx-control-inner-background: #81b4b4");

        root.setTop(userLabel);
        root.setCenter(userList);
    }

    public BorderPane getRoot() {
        return root;
    }

    public void setOnUserSelected(User object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOnUserSelected'");
    }

    
}
