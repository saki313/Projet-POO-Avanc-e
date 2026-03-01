package model;

public class MessageText extends Message {
    private String content;

    public MessageText() {
        super(new User());
    }

    public MessageText(String content, User sender) {
        super(sender);
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    public void setText(String content) {
        this.content = content;
    }
}
