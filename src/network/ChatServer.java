package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer {
    static AtomicInteger numberClient = new AtomicInteger(0);
    static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        final int PORT = 5050;

        try (ServerSocket serverSocket = new ServerSocket(PORT);) {            
            System.out.println("En attente de connexion sur le port " + PORT);

            // Accepter les connexions
            while (true) {
                Socket clientSocket = serverSocket.accept();
                
                int clientID = numberClient.incrementAndGet();
                System.out.println("Client : " + clientID + " connecté : " +  clientSocket.getInetAddress());

                // generer un thread pour gérer chaque client
                ClientHandler client = new ClientHandler(clientID, clientSocket);
                clients.add(client);
                client.start();
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}