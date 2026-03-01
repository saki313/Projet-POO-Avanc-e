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
    }

}