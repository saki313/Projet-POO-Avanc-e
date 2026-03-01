package core;

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
            ServerMain.addUser(out);

            // Demander le nom
            out.println("Bienvenue ! Entrez un pseudo : ");
            username = in.readLine();

            // annoncer à tous qu'il s'est connecté
            int len = ServerMain.getClients().size();
            ServerMain.annonce(username + " s'est connecté (" + len + " connecté" + (len>1? "s":"") + ")");
            
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) break;
                ServerMain.broadcast(username + ": " + message, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    private void closeConnection() {
        ServerMain.getClients().remove(out);

        if (username != null) {
            ServerMain.getNomsClients().remove(out);
            ServerMain.annonce(username + " s'est connecté.");
            System.out.println("Un utilisateur vient de se déconnecter: " + clientSocket.getInetAddress());
        }
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}