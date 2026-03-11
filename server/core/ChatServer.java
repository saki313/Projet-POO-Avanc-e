package core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import manager.ClientManager;

public class ChatServer {
    private static final int PORT = 12345;

    // main
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Le serveur a demarré sur le port " +  PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();

                HandlerClient handler = new core.HandlerClient(clientSocket);
                ClientManager.addUser(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("Erreur serveur: " + e.getMessage());
        }
    }
}
