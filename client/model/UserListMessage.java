package model;

import java.util.List;

public class UserListMessage extends Message {
    private static final long serialVersionUID = 1L;
    private List<User> users;

    public UserListMessage(List<User> users) {
        super(new User("Système"));
        this.users = users;
        this.setVisibility("system");
    }

    public List<User> getUsers() {
        return users;
    }

}
