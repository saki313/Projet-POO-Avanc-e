package core;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HandlerClient implements Runnable {
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
            ChatServer.addUser(out);

            // Demander le nom
            out.println("Bienvenue ! Entrez un pseudo : ");
            username = in.readLine();

            // annoncer à tous qu'il s'est connecté
            int len = ChatServer.getClients().size();
            ChatServer.annonce(username + " s'est connecté (" + len + " connecté" + (len>1? "s":"") + ")");
            
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) break;
                ChatServer.broadcast(username + ": " + message, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    private void closeConnection() {
        ChatServer.getClients().remove(out);

        if (username != null) {
            ChatServer.getNomsClients().remove(out);
            ChatServer.annonce(username + " s'est connecté.");
            System.out.println("Un utilisateur vient de se déconnecter: " + clientSocket.getInetAddress());
        }
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}