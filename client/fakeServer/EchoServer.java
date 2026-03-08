package fakeServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import model.Message;
import model.MessageFile;
import model.MessageText;
import model.User;

public class EchoServer {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server started on port 12345");

            while (true) {

                Socket clientSocket = serverSocket.accept();

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
            User user = (User) in.readObject();
            System.out.println(user.getName() + " s'est connecté !");
            out.writeObject(new MessageText(user.getName() + " s'est connecté !", user));

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
            e.printStackTrace();
        }
    }
}