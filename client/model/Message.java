package model;

import java.io.Serializable;
import java.time.LocalTime;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private User sender;
    private LocalTime timestamp;
    private boolean isMine;
    private String visibility;

    public Message() {
        this.timestamp = LocalTime.now();
        this.sender = new User();
        this.visibility = "public";
    }

    public Message(User sender) {
        this.sender = sender;
        this.timestamp = LocalTime.now();
        this.visibility = "public";
    }
    
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public LocalTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }
    public boolean isMine() {
        return isMine;
    }
    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }
    public String getVisibility() {
        return visibility;
    }
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}