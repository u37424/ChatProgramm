package de.medieninformatik;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private static HashMap<String, ObjectOutputStream> clients;
    private ExecutorService pool;

    public Server(int port) {
        this.port = port;
        this.clients = new HashMap<>();
        try {
            System.out.println(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.pool = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server an Port " + port + " gestartet.");

            while (true) {
                final Socket socket = serverSocket.accept();
                pool.execute(new MessengerService(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, ObjectOutputStream> getClients() {
        return clients;
    }
}
