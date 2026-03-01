import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerMain {
    private static final int PORT = 12345;

    // liste sync
    private static Set<PrintWriter> clients = Collections.synchronizedSet(new HashSet<>());
    private static Map<PrintWriter, String> nomsClients = new HashMap<>();

    // main
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Le serveur a demarré sur le port " +  PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Un nouvel utilisateur vient de se connecter: "+ clientSocket.getInetAddress());

                // Flux IO
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // pourquoi ne pas directement ajouter à ce niveau
                // clients.add(out);
                new Thread(new core.HandlerClient(clientSocket,out, in)).start();

            }
        } catch (IOException e) {
            System.out.println("Erreur serveur: " + e.getMessage());
        }
    }

    // diffusion
    public static void broadcast(String msg, PrintWriter sender) {
        String timestamp = LocalTime.now().toString().substring(0, 5);
        String msgFormated = "[" + timestamp + "] " + msg;

        synchronized(clients) {
            for (PrintWriter client : clients) {
                if (client != sender) {
                    client.println(msgFormated);
                }
            }
        }
    }
    public static void annonce(String msg) {
        String timestamp = LocalTime.now().toString().substring(0, 5);
        String msgFormated = "📢 [" + timestamp + "] " + msg;

        synchronized(clients) {
            for (PrintWriter client : clients) {
                client.println(msgFormated);
            }
        }
    }

    // add and remove user
    public static void addUser(PrintWriter out) {
        clients.add(out);
    }
    public static void removeUser(PrintWriter out) {
        clients.remove(out);
    }
    
    // getters
    public static Set<PrintWriter> getClients() {
        return clients;
    }
    public static Map<PrintWriter, String> getNomsClients() {
        return nomsClients;
    }

}