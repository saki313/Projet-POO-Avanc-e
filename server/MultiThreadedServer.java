package server;
import java.io.*;
import java.net.*;
import java.util.*;

public class MultiThreadedServer {
    // Liste partagée pour stocker les flux de sortie de TOUS les clients connectés
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        final int PORT = 12345;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur de Chat démarré sur le port " + PORT);

            // 1. Nombre de clients infinis (Boucle infinie)
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouveau client connecté : " + clientSocket.getInetAddress());
                
                // Lancer un thread pour chaque client
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // 2. Méthode pour envoyer un message à tout le monde
    public static void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    // Gestionnaire de client (Thread)
    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) { this.socket = socket; }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                // Ajouter le client à la liste de diffusion
                synchronized (clientWriters) { clientWriters.add(out); }

                // 3. Conversation continue
                String msg;
                while ((msg = in.readLine()) != null) {
                    // 4. Gestion de la déconnexion "EXIT"
                    if (msg.equalsIgnoreCase("EXIT")) {
                        break;
                    }
                    System.out.println("Diffusion : " + msg);
                    broadcast("Un client dit : " + msg);
                }
            } catch (IOException e) { e.printStackTrace();
            } finally {
                // Nettoyage à la déconnexion
                try {
                    synchronized (clientWriters) { clientWriters.remove(out); }
                    socket.close();
                    System.out.println("Un client s'est déconnecté.");
                } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
}