package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    public ChatClient() {
    }

    public void sendMessage(String message) {
        
    }

    public static void main(String[] args) {
        final int PORT = 5050;
        final String SERVER_ADDR = "localhost";

        try {
            // Créer un socket
            Socket socket = new Socket(SERVER_ADDR, PORT);
            System.out.println("Connecté au server !");

            // Flux IO
            //PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Lire le message de bienvenue
            System.out.println("Serveur : " + reader.readLine());

            // converser avec le serveur
            // BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            
            new Receiver(socket).start();
            new Sender(socket).start();

            // Lire la reponse du serveur
            // System.out.println("Serveur : " + reader.readLine());

            // Fermer la connexion
            //reader.close();
            //socket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Sender extends Thread {
    private Socket socket;

    public Sender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in)))
        {
            String message;
            do {
                message = consoleReader.readLine();
                if (!message.equals("exit"))
                    writer.println(message);
            } while (!message.equals("exit"));
            
            System.out.println("Déconnecté du serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

class Receiver extends Thread {
    private Socket socket;

    public Receiver(Socket socket) {
        this.socket = socket;
        System.out.println(this.socket.getInetAddress());
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message;
            do {
                message = reader.readLine();
                System.out.println(message);
            } while (true);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}