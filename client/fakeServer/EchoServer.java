package fakeServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import model.Message;
import model.MessageFile;
import model.MessageText;

public class EchoServer {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server started on port 12345");

            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                // Un thread par client
                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {

        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {

            while (true) {

                Message message = (Message) in.readObject();
                if (message instanceof MessageText msgText) {
                    System.out.println("Received: " + msgText.getContent());
                } else {
                    MessageFile msgFile = (MessageFile) message;
                    System.out.println("Received file: " + msgFile.getFileName() + " (" + msgFile.getData().length + " bytes)");
                }

                out.writeObject(message);
                out.flush();
            }

        } catch (Exception e) {
            System.out.println("Client déconnecté");
        }
    }
}