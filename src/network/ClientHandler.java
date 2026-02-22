package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler extends Thread {
    private int clientNumber;
    private Socket clientSocket;


    public ClientHandler(int clientNumber, Socket clientSocket) {
        this.clientNumber = clientNumber;
        this.clientSocket = clientSocket;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    @Override
    public void run() {
        try {
            // Flux IO
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Message de bienvenue
            writer.println("Bienvenue client " + clientNumber);

            // communication
            String message;
            do {
                message = reader.readLine();
                if (message != null && !message.equals("exit")) {
                    broadcast(message, this);
                }
            } while (message != null && !message.equals("exit"));

            // Deconnexion
            reader.close();
            writer.close();
            clientSocket.close();
            System.out.println("Client : " + clientNumber + " déconnecté : " +  clientSocket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : ChatServer.clients) {
            if (client != sender) {
                client.sendMessage(message, sender);
            }
        }

    }

    public void sendMessage(String message, ClientHandler sender) {
        try {
            // Flux I
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        
            writer.println("Client " + sender.getClientNumber() + " : " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}