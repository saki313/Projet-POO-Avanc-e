package network;

import java.io.BufferedReader;
// import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

// import javafx.application.Platform;

public class ChatClientConnection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> onMessageReceived;
    
    public ChatClientConnection() {
    }
    
    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(this::lisen).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lisen() {
        String serverMsg;
        try {
            while ((serverMsg = in.readLine()) != null) {
                if (onMessageReceived != null) {
                    onMessageReceived.accept(serverMsg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        String msg = message.trim();
        if (!msg.isEmpty()) {
            if (out != null) {
                out.println(msg);
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

    public void setOnMessageReceived(Consumer<String> consumer) {
        this.onMessageReceived = consumer;
    }
}