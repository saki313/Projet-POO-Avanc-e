package model;

import java.io.Serializable;
import java.time.LocalDateTime;



public class Message implements Serializable {
    MessageType type;
    LocalDateTime dateTime;
    Object content;
    
    public Message(MessageType type, LocalDateTime dateTime, Object content) {
        this.type = type;
        this.dateTime = dateTime;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    
}
