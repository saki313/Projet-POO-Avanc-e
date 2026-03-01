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

public class Main {
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
                new Thread(new HandlerClient(clientSocket,out, in)).start();

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

class HandlerClient implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public HandlerClient(Socket clientSocket, PrintWriter out, BufferedReader in) {
        this.clientSocket = clientSocket;
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        try {
            // Ajouter ici
            Main.addUser(out);

            // Demander le nom
            out.println("Bienvenue ! Entrez un pseudo : ");
            username = in.readLine();

            // annoncer à tous qu'il s'est connecté
            int len = Main.getClients().size();
            Main.annonce(username + " s'est connecté (" + len + " connecté" + (len>1? "s":"") + ")");
            
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) break;
                Main.broadcast(username + ": " + message, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    private void closeConnection() {
        Main.getClients().remove(out);

        if (username != null) {
            Main.getNomsClients().remove(out);
            Main.annonce(username + " s'est connecté.");
            System.out.println("Un utilisateur vient de se déconnecter: " + clientSocket.getInetAddress());
        }
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}