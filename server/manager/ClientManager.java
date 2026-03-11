package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Message;
import model.User;
import model.UserListMessage;
import core.HandlerClient;

public class ClientManager {
    // liste sync
    private static Set<HandlerClient> clients = Collections.synchronizedSet(new HashSet<>());
    // private static Map<ObjectOutputStream, User> objetClients = new HashMap<>();

    // add and remove user
    public static void addUser(HandlerClient user) {
        clients.add(user);
    }
    public static void removeUser(HandlerClient user) {
        clients.remove(user);
    }

    // diffusion
    public static void broadcast(Message message, HandlerClient sender) {
        synchronized(clients) {
            for (HandlerClient client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }
    public static void annonce(Message msg) {
        synchronized(clients) {
            for (HandlerClient client : clients) {
                client.sendMessage(msg);
            }
        }
    }
    public static void broadcastUserList(String msg) {
        UserListMessage userListMsg = new UserListMessage(getConnectedUsers(), msg);
        annonce(userListMsg);
    }

    public static List<User> getConnectedUsers() {
        List<User> users = new ArrayList<>();
        for (HandlerClient client : clients) {
            if (client.getUser() != null) {
                users.add(client.getUser());
            }
        }
        return users;
    }

    public static void removeClient(HandlerClient client) {
        clients.remove(client);
        broadcast(new UserListMessage(getConnectedUsers()), null);
    }

    // getters
    public static Set<HandlerClient> getClients() {
        return clients;
    }
}
