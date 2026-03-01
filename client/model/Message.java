package model;

import java.time.LocalTime;

public class Message {

    private User sender;
    private LocalTime timestamp;
    private boolean isMine;

    public Message() {
    }

    public Message(User sender) {
        this.sender = sender;
        this.timestamp = LocalTime.now();
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
}
