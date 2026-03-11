package core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import manager.ClientManager;
import model.Message;
import model.MessageFile;
import model.MessageText;
import model.User;

public class HandlerClient implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream ops;
    private ObjectInputStream ips;
    private User user;

    public HandlerClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Flux IO
            ops = new ObjectOutputStream(clientSocket.getOutputStream());
            ips = new ObjectInputStream(clientSocket.getInputStream());

            user = (User) ips.readObject();

            // S'assurer que le nom n'est pas null
            if (user.getName() == null || user.getName().trim().isEmpty()) {
                user.setName("Anonyme");
            }

            // annoncer à tous qu'il s'est connecté
            int len = ClientManager.getClients().size();
            String msg = user.getName() + " s'est connecté (" + len + " connecté" + (len>1? "s":"") + ")";
            ClientManager.broadcastUserList(msg);
            
            System.out.println(msg + " :: " + clientSocket.getInetAddress());
            
            while (true) {
                Message message = (Message) ips.readObject();

                if (message instanceof MessageText msgText) {
                    System.out.println("Message reçu de " + msgText.getSender().getName() + ": " + msgText.getContent());
                } else if (message instanceof MessageFile msgFile) {
                    System.out.println("Fichier reçu de " + msgFile.getSender().getName() + ": " + msgFile.getFileName() + " (" + msgFile.getData().length + " bytes)");
                }

                ClientManager.broadcast(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.toString());
        } finally {
            this.closeConnection();
        }
    }

    public void sendMessage(Message message) {
        try {
            ops.writeObject(message);
            ops.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void    closeConnection() {
        if (user != null) {
            ClientManager.getClients().remove(this);
            ClientManager.broadcastUserList(user.getName() + " s'est connecté.");
            System.out.println("Un utilisateur vient de se déconnecter: " + clientSocket.getInetAddress());
        }
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }
}
