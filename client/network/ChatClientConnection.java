package network;

// import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

import model.Message;

// import javafx.application.Platform;

public class ChatClientConnection {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Consumer<Message> onMessageReceived;
    
    public ChatClientConnection() {
    }
    
    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            new Thread(this::lisen).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lisen() {
        try {
            while (true) {
                Message msg = (Message) in.readObject();
                if (msg != null) {
                    onMessageReceived.accept(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        if (out != null) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnMessageReceived(Consumer<Message> consumer) {
        this.onMessageReceived = consumer;
    }
}